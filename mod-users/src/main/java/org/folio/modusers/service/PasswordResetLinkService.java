package org.folio.modusers.service;

import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.folio.modusers.client.AuthTokenClient;
import org.folio.modusers.client.AuthnClient;
import org.folio.modusers.client.ConfigurationClient;
import org.folio.modusers.client.NotificationClient;
import org.folio.modusers.client.PasswordValidateClient;
import org.folio.modusers.domain.dto.password.AuthTokenDto;
import org.folio.modusers.domain.dto.password.Config;
import org.folio.modusers.domain.dto.password.Configurations;
import org.folio.modusers.domain.dto.password.PasswordResetAction;
import org.folio.modusers.domain.dto.password.ResetPasswordDto;
import org.folio.modusers.dto.GenerateLinkResponseDto;
import org.folio.modusers.dto.UserDto;
import org.folio.modusers.exception.UnprocessableEntityException;
import org.folio.modusers.exception.UnprocessableEntityMessage;
import org.folio.modusers.utils.NotificationUtils;
import org.folio.spring.integration.OkapiService;
import org.folio.spring.integration.XOkapiHeaders;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetLinkService {

  private static final String MODULE_NAME = "SMTP_SERVER";
  private static final String LINK_EXPIRATION_TIME_CONFIG_KEY = "RESET_PASSWORD_LINK_EXPIRATION_TIME";
  private static final String LINK_EXPIRATION_TIME_DEFAULT = "86400000";
  private static final String UNDEFINED_USER_NAME = "UNDEFINED_USER__RESET_PASSWORD_";
  private static final String FOLIO_HOST_CONFIG_KEY = "FOLIO_HOST";
  private static final String FOLIO_HOST_DEFAULT = "http://localhost:3000";
  private static final String UI_PATH_CONFIG_KEY = "RESET_PASSWORD_UI_PATH";
  private static final String RESET_PASSWORD_EVENT_CONFIG_NAME = "RESET_PASSWORD_EVENT";
  private static final String CREATE_PASSWORD_EVENT_CONFIG_NAME = "CREATE_PASSWORD_EVENT";
  private static final String PASSWORD_CREATED_EVENT_CONFIG_NAME = "PASSWORD_CREATED_EVENT";//NOSONAR
  private static final String PASSWORD_CHANGED_EVENT_CONFIG_NAME = "PASSWORD_CHANGED_EVENT";//NOSONAR
  private static final String LINK_INVALID_STATUS_CODE = "link.invalid";

  private String resetPasswordUIPathDefault = System
      .getProperty("reset-password.ui-path.default", "/reset-password");

  private final UserService userService;
  private final ConfigurationClient configurationClient;
  private final AuthnClient authnClient;
  private final AuthTokenClient authTokenClient;
  private final NotificationClient notificationClient;
  private final PasswordValidateClient passwordValidateClient;
  private final OkapiService okapiService;

  public GenerateLinkResponseDto sendPasswordRestLink(String userId) {
    Map<String, String> configurations = convertConfigsToMap(
        configurationClient.lookupConfigByModuleName("module==" + MODULE_NAME));
    UserDto user = lookupAndValidate(userId);
    long expirationTimeFromConfig = Long.parseLong(
        configurations.getOrDefault(LINK_EXPIRATION_TIME_CONFIG_KEY, LINK_EXPIRATION_TIME_DEFAULT));

    final String passwordResetActionId = UUID.randomUUID().toString();
    Boolean isPasswordExists = generatePasswordResetAction(userId, expirationTimeFromConfig,
        passwordResetActionId);

    String generatedLink = generateLink(configurations, getTokenPayload(passwordResetActionId));
    String eventConfigName =
        isPasswordExists ? RESET_PASSWORD_EVENT_CONFIG_NAME : CREATE_PASSWORD_EVENT_CONFIG_NAME;
    sendNotification(user, expirationTimeFromConfig, generatedLink, eventConfigName);
    return new GenerateLinkResponseDto().link(generatedLink);
  }

  private UserDto lookupAndValidate(String userId) {
    UserDto user = userService.getUserById(userId);
    return validateUser(user, userId);
  }

  private String generateLink(Map<String, String> configurations, String token) {
    String linkHost = configurations.getOrDefault(FOLIO_HOST_CONFIG_KEY, FOLIO_HOST_DEFAULT);
    String linkPath = configurations.getOrDefault(UI_PATH_CONFIG_KEY, resetPasswordUIPathDefault);
    return linkHost + linkPath + '/' + token;
  }

  private void sendNotification(UserDto user, long expirationTimeFromConfig,
      String generatedLink, String eventConfigName) {
    String notification = NotificationUtils
        .getNotificationWithExpirationTime(user, eventConfigName, generatedLink,
            expirationTimeFromConfig);

    notificationClient.sendNotification(notification);
  }

  private String getTokenPayload(String passwordResetActionId) {
    String payload = new JSONObject()
        .put("payload", new JSONObject()
            .put("sub", UNDEFINED_USER_NAME + passwordResetActionId)
            .put("dummy", true)
            .put("extra_permissions", new JSONArray()
                .put("users-bl.password-reset-link.validate")
                .put("users-bl.password-reset-link.reset")
            )).toString();

    String token = okapiService.getHeader(XOkapiHeaders.TOKEN)
        .orElseThrow(() -> new IllegalStateException("Request without token"));
    AuthTokenDto authTokenDto = authTokenClient
        .signToken(token, payload);
    return authTokenDto.getToken();
  }

  private Boolean generatePasswordResetAction(String userId, long expirationTimeFromConfig,
      String passwordResetActionId) {
    String action = getPasswordResetAction(expirationTimeFromConfig, userId, passwordResetActionId);
    return authnClient.saveAction(action).getPasswordExists();
  }

  private String getPasswordResetAction(long expirationTimeFromConfig, String userId,
      String passwordResetActionId) {
    return new JSONObject()
        .put("userId", userId)
        .put("id", passwordResetActionId)
        .put("expirationTime",
            Instant.now()
                .plusMillis(expirationTimeFromConfig)
                .toEpochMilli()).toString();
  }

  private UserDto validateUser(UserDto user, String userId) {
    if (Objects.isNull(user)) {
      throw new RuntimeException(String.format("User with id '%s' not found", userId));
    }
    if (StringUtils.isBlank(user.getUsername())) {
      throw new RuntimeException("User without username cannot reset password");
    }
    return user;
  }

  public void validateLink() {
    String passwordResetActionId = getPasswordResetActionId();
    PasswordResetAction action = authnClient.getAction(passwordResetActionId)
        .orElseThrow(throwPasswordResetAction(passwordResetActionId));
    checkPasswordResetActionExpirationTime(action, passwordResetActionId);
    UserDto user = userService.getUserById(action.getUserId());
    if (Objects.isNull(user)) {
      throw new RuntimeException(
          String.format("User with id = %s in not found", action.getUserId()));
    }
  }

  private void checkPasswordResetActionExpirationTime(
      PasswordResetAction passwordResetAction, String passwordResetActionId) {
    if (!passwordResetAction.getExpirationTime().toInstant().isAfter(Instant.now())) {
      throw new RuntimeException(
          String.format("PasswordResetAction with id = %s is expired", passwordResetActionId));
    }
  }

  private String getPasswordResetActionId() {
    String token = okapiService.getHeader(XOkapiHeaders.TOKEN)
        .orElseThrow(() -> new IllegalStateException("Request without token"));
    byte[] decode = Base64.getDecoder().decode(token.split("\\.")[1]);
    JSONObject payload = new JSONObject(new String(decode));
    String tokenSub = payload.getString("sub");

    if (!tokenSub.startsWith(UNDEFINED_USER_NAME)) {
      UnprocessableEntityMessage message = new UnprocessableEntityMessage(LINK_INVALID_STATUS_CODE,
          "Invalid token.");
      throw new UnprocessableEntityException(Collections.singletonList(message));
    }
    return tokenSub.substring(UNDEFINED_USER_NAME.length());
  }

  public void resetPassword(String newPassword) {
    String passwordResetActionId = getPasswordResetActionId();
    PasswordResetAction action = authnClient.getAction(passwordResetActionId)
        .orElseThrow(throwPasswordResetAction(passwordResetActionId));
    checkPasswordResetActionExpirationTime(action, passwordResetActionId);

    UserDto user = lookupAndValidate(passwordResetActionId);
    validatePassword(user.getId(), newPassword);

    String payload = getPayload(newPassword, passwordResetActionId);
    ResetPasswordDto passwordDto = authnClient.resetPassword(payload);
    String notification = createNotification(user, passwordDto.getIsNewPassword());
    notificationClient.sendNotification(notification);
  }

  private String getPayload(String newPassword, String passwordResetActionId) {
    return new JSONObject().put("newPassword", newPassword)
        .put("passwordResetActionId", passwordResetActionId).toString();
  }

  private Supplier<RuntimeException> throwPasswordResetAction(String passwordResetActionId) {
    return () -> new RuntimeException(
        String.format("PasswordResetAction with id = %s is expired", passwordResetActionId));
  }

  private String createNotification(UserDto user, boolean isNewPassword) {
    String eventConfigName = isNewPassword
        ? PASSWORD_CREATED_EVENT_CONFIG_NAME
        : PASSWORD_CHANGED_EVENT_CONFIG_NAME;
    return NotificationUtils.getNotification(user, eventConfigName);
  }

  private void validatePassword(String userId, String newPassword) {
    passwordValidateClient.validateNewPassword(buildPasswordEntity(userId, newPassword));
  }

  private Map<String, String> convertConfigsToMap(Configurations configurations) {
    return configurations.getConfigs().stream()
        .filter(Config::getEnabled)
        .collect(Collectors.toMap(Config::getCode, Config::getValue));
  }

  private String buildPasswordEntity(String userId, String newPassword) {
    return new JSONObject()
        .put("password", newPassword)
        .put("userId", userId).toString();
  }
}
