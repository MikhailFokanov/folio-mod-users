package org.folio.modusers.client;

import org.folio.modusers.domain.dto.password.AuthTokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient("token")
public interface AuthTokenClient {

  @PostMapping
  AuthTokenDto signToken(@RequestHeader("X-Okapi-Token") String token,
      @RequestBody String tokenPayload);
}
