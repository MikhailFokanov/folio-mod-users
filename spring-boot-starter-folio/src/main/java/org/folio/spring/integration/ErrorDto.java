package org.folio.spring.integration;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ErrorDto {

  private String message;

  private String type;

  private String code;

  private List<Map<String, Object>> parameters;

}

