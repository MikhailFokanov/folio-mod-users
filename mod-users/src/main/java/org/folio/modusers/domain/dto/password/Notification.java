
package org.folio.modusers.domain.dto.password;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import javax.validation.Valid;
import lombok.Data;


/**
 * Notification schema for mod-notify
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Notification {

  /**
   * Unique event config name
   */
  @JsonPropertyDescription("Unique event config name")
  private String eventConfigName;
  /**
   * The UUID of the receiving user
   */
  @JsonPropertyDescription("The UUID of the receiving user")
  private String recipientId;
  /**
   * The text of this notification
   */
  @JsonPropertyDescription("The text of this notification")
  private String text;
  /**
   * Notification language
   */
  @JsonPropertyDescription("Notification language")
  private String lang;
  /**
   * Context object
   */
  @JsonPropertyDescription("Context object")
  @Valid
  private Context context;

}
