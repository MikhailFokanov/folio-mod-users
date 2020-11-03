package org.folio.modusers.mapper;

import org.folio.modusers.domain.entity.Address;
import org.folio.modusers.dto.UserPersonalAddressesDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AddressMapper {

  @Mappings({
      @Mapping(target = "id", expression =
          "java(address.getId() == null ? null : String.valueOf(address.getId()"
              + "))"),
      @Mapping(target = "addressTypeId", expression =
          "java(address.getAddressType().getId() == null ? null : String.valueOf(address.getAddressType().getId()"
              + "))")
  })
  UserPersonalAddressesDto mapEntityToDto(Address address);

  @Mappings({
      @Mapping(target = "id", expression =
          "java(userPersonalAddressesDto.getId() == null ? null : java.util.UUID.fromString"
              + "(userPersonalAddressesDto.getId()))"),
      @Mapping(target = "addressType.id", expression =
          "java(userPersonalAddressesDto.getAddressTypeId() == null ? null : java.util.UUID.fromString"
              + "(userPersonalAddressesDto.getAddressTypeId()))"),
      @Mapping(target = "user", ignore = true)

  })
  @InheritInverseConfiguration
  Address mapDtoToEntity(UserPersonalAddressesDto userPersonalAddressesDto);
}
