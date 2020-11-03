package org.folio.modusers.controller;

import static org.springframework.data.util.CastUtils.cast;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.folio.modusers.blusers.api.BlUsersApi;
import org.folio.modusers.client.NotificationClient;
import org.folio.modusers.dto.CompositeUserDto;
import org.folio.modusers.dto.CompositeUserListObjectDto;
import org.folio.modusers.dto.GenerateLinkRequestDto;
import org.folio.modusers.dto.GenerateLinkResponseDto;
import org.folio.modusers.dto.IdentifierDto;
import org.folio.modusers.dto.LoginCredentialsDto;
import org.folio.modusers.dto.PasswordResetDto;
import org.folio.modusers.dto.UpdateCredentialsDto;
import org.folio.modusers.dto.UserDto;
import org.folio.modusers.enums.UserPropertyInclude;
import org.folio.modusers.service.CompositeUserService;
import org.folio.modusers.service.PasswordResetLinkService;
import org.folio.modusers.service.UserService;
import org.folio.modusers.utils.NotificationUtils;
import org.folio.spring.integration.OkapiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bl-users-poc")
@RequiredArgsConstructor
public class CompositeUserController implements BlUsersApi {

  private static final String USERNAME_LOCATED_EVENT_CONFIG_NAME = "USERNAME_LOCATED_EVENT";

  private final CompositeUserService compositeUserService;
  private final PasswordResetLinkService passwordResetLinkService;
  private final NotificationClient notificationClient;
  private final OkapiService okapiService;
  private final UserService userService;

  @Override
  public ResponseEntity<CompositeUserDto> getBlUserById(@PathVariable("id") String id,
      @Valid List<String> include,
      @Valid Boolean expandPermissions) {
    return compositeUserService
        .getUserById(id, UserPropertyInclude.from(include), expandPermissions)
        .map(ResponseEntity::ok)
        .orElse(cast(notFound()));
  }

  @Override
  public ResponseEntity<CompositeUserListObjectDto> getBlUsers(@Valid String query,
      @Valid List<String> include, @Valid Integer offset, @Valid Integer limit) {
    return ok(compositeUserService.getUsers(offset, limit, UserPropertyInclude.from(include)));
  }

  @Override
  public ResponseEntity<CompositeUserDto> getBlUsersSelf(
      @RequestParam(value = "include", required = false) List<String> include,
      @RequestParam(value = "expandPermissions", required = false, defaultValue = "false") Boolean expandPermissions) {
    return okapiService.getUsername()
        .flatMap(e -> compositeUserService
            .getUserByUsername(e, UserPropertyInclude.from(include), expandPermissions))
        .map(ResponseEntity::ok)
        .orElse(cast(notFound()));
  }

  @Override
  public ResponseEntity<CompositeUserDto> getBlUserByUserName(String username,
      List<String> include, Boolean expandPermissions) {
    return compositeUserService
        .getUserByUsername(username, UserPropertyInclude.from(include), expandPermissions)
        .map(ResponseEntity::ok)
        .orElse(cast(notFound()));
  }

  @Override
  public ResponseEntity<CompositeUserDto> loginBlUser(String userAgent, String xForwardedFor,
      @Valid List<String> include, @Valid Boolean expandPermissions,
      @Valid LoginCredentialsDto loginCredentials) {
    return null;
  }

  @Override
  public ResponseEntity<GenerateLinkResponseDto> sendPasswordRestLink(
      @Valid GenerateLinkRequestDto generateLinkRequest) {
    return new ResponseEntity<>(
        passwordResetLinkService.sendPasswordRestLink(generateLinkRequest.getUserId()),
        HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> sendPasswordRestLink(String userAgent, String xForwardedFor,
      @Valid PasswordResetDto passwordReset) {
    passwordResetLinkService.resetPassword(passwordReset.getNewPassword());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> usersForgottenPassword(@Valid IdentifierDto entity) {
    UserDto user = userService.getUserByAlias(entity.getId());
    passwordResetLinkService.sendPasswordRestLink(String.valueOf(user.getId()));
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> usersForgottenUsername(@Valid IdentifierDto entity) {
    UserDto user = userService.getUserByAlias(entity.getId());
    notificationClient.sendNotification(
        NotificationUtils.getNotification(user, USERNAME_LOCATED_EVENT_CONFIG_NAME));
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Object> usersSettingsMyProfilePassword(String userAgent,
      String xForwardedFor, @Valid UpdateCredentialsDto indetifier) {
    return null;
  }

  @Override
  public ResponseEntity<Void> validatePasswordResetLink() {
    passwordResetLinkService.validateLink();
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
