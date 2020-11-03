package org.folio.modusers.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.folio.modusers.domain.entity.AddressType;
import org.folio.modusers.dto.AddresstypeCollectionDto;
import org.folio.modusers.dto.AddresstypeDto;
import org.folio.modusers.mapper.AddressTypeMapper;
import org.folio.modusers.repository.AddressTypeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressTypeService {
    private final AddressTypeRepository addressTypeRepository;

    private final AddressTypeMapper addressTypeMapper;

    public AddresstypeCollectionDto getAddressTypes() {
        return addressTypeMapper.mapToUserDataCollectionDto(addressTypeRepository.findAll());
    }

    public AddresstypeDto getAddressTypeById(final String addressTypeId) {
        return addressTypeRepository.findById(UUID.fromString(addressTypeId))
                .map(addressTypeMapper::mapEntityToDto)
                .orElseThrow(() -> new IllegalArgumentException("Address type not found"));
    }

    public void deleteAddressTypes(final String addressTypeId) {
        addressTypeRepository.deleteById(UUID.fromString(addressTypeId));
    }

    public AddresstypeDto saveAddressTypes(final AddresstypeDto addressTypeDto) {
        AddressType addressType = addressTypeMapper.mapDtoToEntity(addressTypeDto);
        return addressTypeMapper.mapEntityToDto(addressTypeRepository.save(addressType));
    }

    public void updateAddressTypes(final AddresstypeDto addressTypeDto,final String addressTypeId ) {
        AddressType addressType = addressTypeRepository.getOne(UUID.fromString(addressTypeId));
        addressTypeMapper.mapEntityToDto(addressTypeDto, addressType);
        addressTypeRepository.save(addressType);
    }
}
