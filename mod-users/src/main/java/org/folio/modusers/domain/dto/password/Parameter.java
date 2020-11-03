package org.folio.modusers.domain.dto.password;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class Parameter {

  private String key;

  private String value;

  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap();

  public Parameter() {
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String var1, Object var2) {
    this.additionalProperties.put(var1, var2);
  }

  public Parameter withAdditionalProperty(String var1, Object var2) {
    this.additionalProperties.put(var1, var2);
    return this;
  }
}
