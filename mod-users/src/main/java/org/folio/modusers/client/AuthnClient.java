package org.folio.modusers.client;

import java.util.Optional;
import org.folio.modusers.domain.dto.ResultList;
import org.folio.modusers.domain.dto.password.PasswordResetAction;
import org.folio.modusers.domain.dto.password.PasswordResetActionDto;
import org.folio.modusers.domain.dto.password.ResetPasswordDto;
import org.folio.modusers.dto.CredentialsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("authn")
public interface AuthnClient extends QueryableClient<CredentialsDto> {

  @GetMapping("credentials")
  @Override
  ResultList<CredentialsDto> query(@RequestParam("query") String query);

  /**
   * Saves given action
   *
   * @param passwordResetAction entry to save
   * @return password with field true if password already exists
   */
  @PostMapping(value = "password-reset-action", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  PasswordResetActionDto saveAction(@RequestBody String passwordResetAction);

  /**
   * Retrieves password reset action with given id
   *
   * @param passwordResetActionId password reset action id
   * @return password reset action
   */
  @GetMapping("password-reset-action/{passwordResetActionId}")
  Optional<PasswordResetAction> getAction(
      @PathVariable("passwordResetActionId") String passwordResetActionId);

  /**
   * Resets password
   *
   * @return succeeded future if password is reset, otherwise - failed future
   */
  @PostMapping(value = "reset-password", produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  ResetPasswordDto resetPassword(@RequestBody String payload);

}
