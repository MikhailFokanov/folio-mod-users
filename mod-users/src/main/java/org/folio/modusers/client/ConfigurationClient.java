package org.folio.modusers.client;

import org.folio.modusers.domain.dto.password.Configurations;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("configurations")
public interface ConfigurationClient {

  @GetMapping("entries")
  Configurations lookupConfigByModuleName(@RequestParam("query") String query);

}
