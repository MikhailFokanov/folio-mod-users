package org.folio.modusers.mapper;

import java.util.List;
import org.folio.modusers.domain.entity.Address;
import org.folio.modusers.domain.entity.ProxyFor;
import org.folio.modusers.domain.entity.User;
import org.folio.modusers.dto.UserDto;
import org.folio.modusers.dto.UserdataCollectionDto;
import org.folio.modusers.utils.MappingUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.domain.Page;


@Mapper(componentModel = "spring", uses = {AddressMapper.class, ProxyForMapper.class},
    imports = {MappingUtils.class}, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

  @Mappings({
      @Mapping(target = "id", expression = "java(MappingUtils.toStr(user.getId()))"),
      @Mapping(target = "externalSystemId", expression =
          "java(MappingUtils.toStr(user.getExternalSystemId()))"),
      @Mapping(target = "patronGroup", expression =
          "java(user.getUserGroup() == null || user.getUserGroup().getId() == null  ? null : String.valueOf(user.getUserGroup().getId()))"),
      @Mapping(target = "personal.firstName", source = "firstname"),
      @Mapping(target = "personal.lastName", source = "lastname"),
      @Mapping(target = "personal.middleName", source = "middlename"),
      @Mapping(target = "personal.email", source = "email"),
      @Mapping(target = "personal.phone", source = "phone"),
      @Mapping(target = "personal.mobilePhone", source = "mobilePhone"),
      @Mapping(target = "personal.dateOfBirth", source = "dateOfBirth"),
      @Mapping(target = "metadata.createdDate", source = "createdDate"),
      @Mapping(target = "metadata.updatedDate", source = "updatedDate")
  })
  UserDto mapEntityToDto(User user);

  @Mappings({
      @Mapping(target = "id", expression =
          "java(MappingUtils.parseUUID(userDto.getId()))"),
      @Mapping(target = "externalSystemId", expression =
          "java(MappingUtils.parseUUID(userDto.getExternalSystemId()))"),
      @Mapping(target = "userGroup.id", expression =
          "java(MappingUtils.parseUUID(userDto.getPatronGroup()))")
  })
  @InheritInverseConfiguration
  User mapDtoToEntity(UserDto userDto);

  @Mappings({})
  List<UserDto> mapEntitiesToDtos(List<User> users);

  @InheritInverseConfiguration
  List<User> mapDtosToEntities(List<UserDto> users);

  @Mappings({
      @Mapping(target = "id", expression = "java(MappingUtils.parseUUID(userDto.getId()))"),
      @Mapping(target = "externalSystemId", expression =
          "java(MappingUtils.parseUUID(userDto.getId()))"),
      @Mapping(target = "userGroup.id", expression =
          "java(MappingUtils.parseUUID(userDto.getPatronGroup()))")
  })
  void mapEntityToDto(UserDto userDto, @MappingTarget User user);

  @AfterMapping
  default void populateUserToAddresses(@MappingTarget User user) {
    if (user.getAddresses() != null) {
      for (Address address : user.getAddresses()) {
        address.setUser(user);
      }
    }
  }

  @AfterMapping
  default void populateUserToProxyFor(@MappingTarget User user) {
    if (user.getProxyFor() != null) {
      for (ProxyFor proxyFor : user.getProxyFor()) {
        proxyFor.setProxyUser(user);
      }
    }
  }

  default UserdataCollectionDto mapToUserDataCollectionDto(Page<User> users) {
    UserdataCollectionDto userdataCollectionDto = new UserdataCollectionDto();
    List<UserDto> userDtoList = mapEntitiesToDtos(users.getContent());
    userdataCollectionDto.setUsers(userDtoList);
    userdataCollectionDto.setTotalRecords(userDtoList.size());
    return userdataCollectionDto;
  }

  default List<User> mapUserDataCollectionDtoToEntity(UserdataCollectionDto userdataCollectionDto) {
    return mapDtosToEntities(userdataCollectionDto.getUsers());
  }

  default String mapProxyForEntityToString(ProxyFor proxyFor) {
    return proxyFor.getId() == null ? null : String.valueOf(proxyFor.getUser().getId());
  }

  default ProxyFor mapProxyForStringToEntity(String proxyForId) {
    User user = new User();
    user.setId(java.util.UUID.fromString(proxyForId));
    ProxyFor proxyFor = new ProxyFor();
    proxyFor.setUser(user);
    return proxyFor;
  }

}
