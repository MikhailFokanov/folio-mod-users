package org.folio.modusers.client;

import org.folio.modusers.domain.dto.ResultList;
import org.folio.modusers.dto.ServicePointDto;
import org.folio.modusers.dto.ServicePointsExpandedUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-points-users")
public interface ServicePointsClient extends QueryableClient<ServicePointsExpandedUserDto> {

  @GetMapping
  ResultList<ServicePointsExpandedUserDto> query(@RequestParam("query") String query);

  @GetMapping
  ResultList<ServicePointDto> expandedQuery(@RequestParam("query") String query);

}
