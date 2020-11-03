package org.folio.modusers.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.folio.modusers.domain.entity.UserGroup;
import org.folio.modusers.dto.UserGroupDto;
import org.folio.modusers.dto.UserGroupsDto;
import org.folio.modusers.mapper.UserGroupMapper;
import org.folio.modusers.repository.UserGroupRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupService {

  private final UserGroupRepository userGroupRepository;

  private final UserGroupMapper userGroupMapper;

  public UserGroupsDto getUserGroups() {
    return userGroupMapper.mapToUserDataCollectionDto(userGroupRepository.findAll());
  }

  public UserGroupDto getUserGroupById(final String userGroupId) {
    return userGroupRepository.findById(UUID.fromString(userGroupId))
        .map(userGroupMapper::mapEntityToDto)
        .orElseThrow(() -> new IllegalArgumentException("User group type not found"));
  }

  public void deleteUserGroup(final String userGroupId) {
    userGroupRepository.deleteById(UUID.fromString(userGroupId));
  }

  public UserGroupDto saveUserGroup(final UserGroupDto userGroupDto) {
    UserGroup userGroup = userGroupMapper.mapDtoToEntity(userGroupDto);
    return userGroupMapper.mapEntityToDto(userGroupRepository.save(userGroup));
  }

  public void updateUserGroup(final UserGroupDto userGroupDto, final String userGroupId) {
    UserGroup userGroup = userGroupRepository.getOne(UUID.fromString(userGroupId));
    userGroupMapper.mapEntityToDto(userGroupDto, userGroup);
    userGroupRepository.save(userGroup);
  }
}
