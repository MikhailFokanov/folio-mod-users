package org.folio.modusers.mapper;

import java.util.List;
import org.folio.modusers.domain.entity.ProxyFor;
import org.folio.modusers.dto.ProxyForDto;
import org.folio.modusers.dto.ProxyforCollectionDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProxyForMapper {

  @Mappings({
      @Mapping(target = "id", expression = "java(proxyFor.getId() == null ? null : String.valueOf(proxyFor.getId()))"),
      @Mapping(target = "userId", expression = "java(proxyFor.getUser() == null || proxyFor.getUser().getId() == null ? null : String.valueOf(proxyFor.getUser().getId()))"),
      @Mapping(target = "proxyUserId", expression = "java(proxyFor.getProxyUser() == null || proxyFor.getProxyUser().getId() == null ? null : String.valueOf(proxyFor.getProxyUser().getId()))")
  })
  ProxyForDto mapEntityToDto(ProxyFor proxyFor);

  @Mappings({
      @Mapping(target = "id", expression = "java(proxyForDto.getId() == null ? null : java.util.UUID.fromString(proxyForDto.getId()))"),
      @Mapping(target = "user.id", expression = "java(proxyForDto.getUserId() == null ? null : java.util.UUID.fromString(proxyForDto.getUserId()))"),
      @Mapping(target = "proxyUser.id", expression = "java(proxyForDto.getProxyUserId() == null ? null : java.util.UUID.fromString(proxyForDto.getProxyUserId()))")
  })
  ProxyFor mapDtoToEntity(ProxyForDto proxyForDto);


  @Mappings({})
  List<ProxyForDto> mapEntitiesToDtos(List<ProxyFor> proxyFors);

  @InheritInverseConfiguration
  List<ProxyFor> mapDtosToEntities(List<ProxyForDto> proxyForDtos);

  default ProxyforCollectionDto mapToProxyForDataCollectionDto(List<ProxyFor> proxyFors) {
    ProxyforCollectionDto proxyforCollectionDto = new ProxyforCollectionDto();
    List<ProxyForDto> proxyForDtos = mapEntitiesToDtos(proxyFors);
    proxyforCollectionDto.setProxiesFor(proxyForDtos);
    proxyforCollectionDto.setTotalRecords(proxyForDtos.size());
    return proxyforCollectionDto;
  }

  default List<ProxyFor> mapProxyForCollectionDtoToEntity(
      ProxyforCollectionDto proxyforCollectionDto) {
    return mapDtosToEntities(proxyforCollectionDto.getProxiesFor());
  }

  @Mappings({
      @Mapping(target = "id", expression = "java(proxyFor.getId() == null ? null : java.util.UUID.fromString(proxyForDto.getId()))"),
  })
  void mapEntityToDto(ProxyForDto proxyForDto, @MappingTarget ProxyFor proxyFor);

}
