
package org.folio.modusers.domain.dto.password;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;


/**
 * Reset password action entity
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "userId",
    "expirationTime"
})
@Data
public class PasswordResetAction {

  /**
   * ID of the password reset action (Required)
   */
  @JsonPropertyDescription("ID of the password reset action")
  @NotNull
  private String id;
  /**
   * User ID to register password reset action (Required)
   */
  @JsonPropertyDescription("User ID to register password reset action")
  @NotNull
  private String userId;
  /**
   * Action expiration time (Required)
   */
  @JsonPropertyDescription("Action expiration time")
  @NotNull
  private Date expirationTime;


}
