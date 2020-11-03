package org.folio.modusers.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.folio.modusers.domain.entity.User;
import org.folio.modusers.dto.UserDto;
import org.folio.modusers.dto.UserdataCollectionDto;
import org.folio.modusers.mapper.UserMapper;
import org.folio.modusers.service.UserService;
import org.folio.modusers.users.api.UsersApi;
import org.folio.spring.service.CQLService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users-poc")
@RequiredArgsConstructor
public class UserController implements UsersApi {

  private final UserService userService;

  private final CQLService cqlService;

  private final UserMapper userMapper;

  @GetMapping("query")
  public List<UserDto> postUsers(@RequestParam String cql,
      @RequestParam(required = false) Integer offset,
      @RequestParam(required = false) Integer limit) {
    return userMapper.mapEntitiesToDtos(cqlService
        .getByCQL(User.class, cql, offset != null ? offset : 0, limit != null ? limit : 100));
  }


  @Override
  public ResponseEntity<UserDto> postUsers(@Valid final UserDto userdata,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid final String lang) {
    return ResponseEntity.ok(userService.saveUser(userdata));
  }

  @Override
  public ResponseEntity<Void> deleteUsersByUserId(final String userId,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid final String lang) {
    userService.removeById(userId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserdataCollectionDto> getUsers(@Valid final String query,
      @Min(0) @Max(2147483647) @Valid final Integer offset,
      @Min(0) @Max(2147483647) @Valid final Integer limit,
      @Valid final String orderBy, @Valid final String order,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid final String lang,
      @Valid final List<String> facets) {
    return ResponseEntity.ok(userService.getUsers(query, offset, limit, order, lang));
  }

  @Override
  public ResponseEntity<UserDto> getUsersByUserId(final String userId,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid final String lang) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @Override
  public ResponseEntity<Void> putUsersByUserId(final String userId,
      @Valid final UserDto userdataCollection,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid final String lang) {
    userService.saveUser(userdataCollection);
    return ResponseEntity.ok().build();
  }
}
