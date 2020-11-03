package org.folio.modusers.domain.dto.password;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TenantAttributes {

  @JsonProperty("module_from")
  @JsonPropertyDescription("The module ID that is being upgraded or disabled")
  private String moduleFrom;

  @JsonProperty("module_to")
  @JsonPropertyDescription("The module ID that is being upgraded or enabled")
  @NotNull
  private String moduleTo;

  @JsonProperty("parameters")
  @JsonPropertyDescription("List of key/value parameters of an error")
  @Valid
  private List<Parameter> parameters = new ArrayList();

  @JsonIgnore
  @Valid
  private Map<String, Object> additionalProperties = new HashMap();

  public TenantAttributes() {
  }

  @JsonProperty("module_from")
  public String getModuleFrom() {
    return this.moduleFrom;
  }

  @JsonProperty("module_from")
  public void setModuleFrom(String var1) {
    this.moduleFrom = var1;
  }

  public TenantAttributes withModuleFrom(String var1) {
    this.moduleFrom = var1;
    return this;
  }

  @JsonProperty("module_to")
  public String getModuleTo() {
    return this.moduleTo;
  }

  @JsonProperty("module_to")
  public void setModuleTo(String var1) {
    this.moduleTo = var1;
  }

  public TenantAttributes withModuleTo(String var1) {
    this.moduleTo = var1;
    return this;
  }

  @JsonProperty("parameters")
  public List<Parameter> getParameters() {
    return this.parameters;
  }

  @JsonProperty("parameters")
  public void setParameters(List<Parameter> var1) {
    this.parameters = var1;
  }

  public TenantAttributes withParameters(List<Parameter> var1) {
    this.parameters = var1;
    return this;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String var1, Object var2) {
    this.additionalProperties.put(var1, var2);
  }

  public TenantAttributes withAdditionalProperty(String var1, Object var2) {
    this.additionalProperties.put(var1, var2);
    return this;
  }
}
