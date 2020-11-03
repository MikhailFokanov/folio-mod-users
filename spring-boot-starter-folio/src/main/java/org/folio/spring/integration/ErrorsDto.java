package org.folio.spring.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * A set of errors
 */
@Data
public class ErrorsDto {

  private List<ErrorDto> errors = null;

  @JsonProperty("total_records")
  private Integer totalRecords = null;
}

