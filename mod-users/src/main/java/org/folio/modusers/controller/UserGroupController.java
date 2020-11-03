package org.folio.modusers.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.folio.modusers.dto.UserGroupDto;
import org.folio.modusers.dto.UserGroupsDto;
import org.folio.modusers.groups.api.GroupsApi;
import org.folio.modusers.service.UserGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users-poc")
@RequiredArgsConstructor
public class UserGroupController implements GroupsApi {

  private final UserGroupService userGroupService;

  @Override
  public ResponseEntity<Void> deleteGroupById(String groupId,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    userGroupService.deleteUserGroup(groupId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<UserGroupDto> getGroupById(String groupId,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    return new ResponseEntity<>(userGroupService.getUserGroupById(groupId), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserGroupsDto> getGroups(@Valid String query,
      @Min(0) @Max(2147483647) @Valid Integer offset, @Min(0) @Max(2147483647) @Valid Integer limit,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    return new ResponseEntity<>(userGroupService.getUserGroups(), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<UserGroupDto> postGroups(@Valid UserGroupDto userGroup,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    return new ResponseEntity<>(userGroupService.saveUserGroup(userGroup), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> putGroupById(String groupId, @Valid UserGroupDto userdataCollection,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    userGroupService.updateUserGroup(userdataCollection, groupId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
