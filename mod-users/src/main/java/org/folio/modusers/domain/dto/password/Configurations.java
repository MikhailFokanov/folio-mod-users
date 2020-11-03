
package org.folio.modusers.domain.dto.password;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;


/**
 * Configuration List
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "configs",
    "totalRecords",
    "resultInfo"
})
@Data
public class Configurations {

  /**
   * configurations (Required)
   */
  @JsonPropertyDescription("configurations")
  @Valid
  @NotNull
  private List<Config> configs = new ArrayList<Config>();
  /**
   * total records (Required)
   */
  @JsonPropertyDescription("total records")
  @NotNull
  private Integer totalRecords;
  /**
   * info
   */
  @JsonPropertyDescription("info")
  @Null
  private Object resultInfo;

}
