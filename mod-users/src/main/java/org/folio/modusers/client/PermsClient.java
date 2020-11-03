package org.folio.modusers.client;

import org.folio.modusers.domain.dto.FullPermissions;
import org.folio.modusers.domain.dto.ResultList;
import org.folio.modusers.dto.PermissionUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("perms")
public interface PermsClient extends QueryableClient<PermissionUserDto> {

  @GetMapping("users")
  @Override
  ResultList<PermissionUserDto> query(@RequestParam("query") String query);

  @GetMapping("users/{userId}/permissions?expanded=true&full=true")
  FullPermissions getFullPerms(@PathVariable("userId") String userId);
}
