package org.folio.modusers.service;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.folio.modusers.enums.UserPropertyInclude.CREDENTIALS_INCLUDE;
import static org.folio.modusers.enums.UserPropertyInclude.EXPAND_PERMS_INCLUDE;
import static org.folio.modusers.enums.UserPropertyInclude.GROUPS_INCLUDE;
import static org.folio.modusers.enums.UserPropertyInclude.PERMS_INCLUDE;
import static org.folio.modusers.enums.UserPropertyInclude.SERVICEPOINTS_INCLUDE;
import static org.springframework.data.util.CastUtils.cast;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.folio.modusers.client.AuthnClient;
import org.folio.modusers.client.PermsClient;
import org.folio.modusers.client.ServicePointsClient;
import org.folio.modusers.domain.dto.FullPermissions;
import org.folio.modusers.domain.dto.ResultList;
import org.folio.modusers.dto.CompositeUserDto;
import org.folio.modusers.dto.CompositeUserListObjectDto;
import org.folio.modusers.dto.PermissionUserDto;
import org.folio.modusers.dto.ServicePointDto;
import org.folio.modusers.dto.ServicePointsExpandedUserDto;
import org.folio.modusers.dto.UserDto;
import org.folio.modusers.enums.UserPropertyInclude;
import org.folio.spring.async.AsyncService;
import org.folio.spring.async.DataFuture;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompositeUserService {

  private final AsyncService async;
  private final UserService userService;

  private final PermsClient permsClient;
  private final AuthnClient authnClient;
  private final ServicePointsClient servicePointsClient;
  public List<UserPropertyInclude> DEFAULT_INCLUDES = asList(GROUPS_INCLUDE, PERMS_INCLUDE,
      SERVICEPOINTS_INCLUDE);


  public Optional<CompositeUserDto> getUserByUsername(String userName,
      List<UserPropertyInclude> include,
      boolean expandPerms) {
    return userService.getUserByUsername(userName, convertToProps(include))
        .map(e -> createCompositeUser(e, include, expandPerms).retrieveData());
  }

  public Optional<CompositeUserDto> getUserById(String userId, List<UserPropertyInclude> include,
      boolean expandPerms) {
    return userService.getUserById(userId, convertToProps(include))
        .map(e -> createCompositeUser(e, include, expandPerms).retrieveData());
  }


  private DataFuture<CompositeUserDto> createCompositeUser(UserDto user,
      List<UserPropertyInclude> include,
      boolean expandPerms) {
    if (isEmpty(include)) {
      include = DEFAULT_INCLUDES;
    }
    CompositeUserDto dto = new CompositeUserDto().user(user);
    Map<UserPropertyInclude, CompletableFuture> list = new HashMap<>();
    if (include.contains(PERMS_INCLUDE)) {
      list.put(PERMS_INCLUDE,
          async.run(() -> enrichPermissions(dto, user.getId())));
    }
    if (include.contains(CREDENTIALS_INCLUDE)) {
      list.put(CREDENTIALS_INCLUDE,
          async.run(() -> enrichCredentials(dto, user.getId())));
    }
    if (include.contains(SERVICEPOINTS_INCLUDE)) {
      list.put(SERVICEPOINTS_INCLUDE,
          async.run(() -> enrichServiceEndpoints(dto, user.getId())));
    }
    if (expandPerms) {
      list.put(EXPAND_PERMS_INCLUDE,
          async.run(() -> {
            list.get(PERMS_INCLUDE).join();
            enrichFullPermissions(dto);
          }));
    }
    return DataFuture
        .of(dto, CompletableFuture.allOf(list.values().toArray(new CompletableFuture[0])));
  }

  private ServicePointsExpandedUserDto expandServicePoints(
      ServicePointsExpandedUserDto servicePointDto) {
    if (servicePointDto == null) {
      return null;
    }

    List<String> servicePointIdQueryList = new ArrayList<>();

    ofNullable(servicePointDto.getDefaultServicePointId())
        .ifPresent(servicePointIdQueryList::add);
    servicePointIdQueryList.addAll(servicePointDto.getServicePointsIds());
    if (servicePointIdQueryList.isEmpty()) {
      return servicePointDto;
    }
    String query = servicePointIdQueryList.stream()
        .filter(Objects::nonNull).map(e -> String.format("id==\"%s\"", e))
        .collect(Collectors.joining(" or "));
    ResultList<ServicePointDto> resultList = servicePointsClient
        .expandedQuery(query);
    servicePointDto.setServicePoints(resultList.getResult());
    return servicePointDto;
  }

  private void enrichServiceEndpoints(CompositeUserDto dto, String userId) {
    requireOneResult(() -> servicePointsClient.query("userId==" + userId),
        PERMS_INCLUDE).map(this::expandServicePoints).ifPresent(dto::setServicePointsUser);
  }

  public CompositeUserListObjectDto getUsers(int offset, int limit,
      List<UserPropertyInclude> include) {
    List<UserDto> userDtos = userService
        .getUsersByCriteria(offset, limit, null, convertToProps(include));
    CompositeUserListObjectDto compositeUserListObjectDto = new CompositeUserListObjectDto();
    compositeUserListObjectDto
        .setCompositeUsers(userDtos.stream()
            .map(userDto -> createCompositeUser(userDto, include, false))
            .map(DataFuture::retrieveData)
            .collect(Collectors.toList()));
    return compositeUserListObjectDto;
  }

  private List<String> convertToProps(List<UserPropertyInclude> include) {
    return include.stream().filter(e -> e.getEntityName() != null)
        .map(UserPropertyInclude::getEntityName).collect(Collectors.toList());
  }

  private void enrichPermissions(CompositeUserDto dto, String userId) {
    requireOneResult(() -> permsClient.query("userId==" + userId),
        PERMS_INCLUDE).ifPresent(dto::setPermissions);
  }


  private <E> Optional<E> requireOneResult(Supplier<ResultList<E>> supplier,
      UserPropertyInclude name) {
    ResultList<E> res = supplier.get();

    if (res == null || res.getTotalRecords() == 0) {
      async.getContext().addError("No record found for query " + name);
      return empty();
    }
    if (res.getTotalRecords() > 1) {
      async.getContext().addError("Multiple record found for query " + name);
      return empty();
    }
    return ofNullable(res.getResult().get(0));
  }

  private void enrichFullPermissions(CompositeUserDto user) {
    FullPermissions fullPerms = permsClient.getFullPerms(user.getPermissions().getId());
    PermissionUserDto permissionUserDto = new PermissionUserDto();
    permissionUserDto.setPermissions(cast(fullPerms.getPermissionNames()));
    user.setPermissions(permissionUserDto);
  }

  private void enrichCredentials(CompositeUserDto dto, String userId) {
    requireOneResult(() -> authnClient.query("userId==" + userId),
        CREDENTIALS_INCLUDE).ifPresent(dto::setCredentials);
  }

}
