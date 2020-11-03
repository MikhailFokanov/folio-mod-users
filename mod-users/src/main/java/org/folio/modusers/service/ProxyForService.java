package org.folio.modusers.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.folio.modusers.domain.entity.ProxyFor;
import org.folio.modusers.dto.ProxyForDto;
import org.folio.modusers.dto.ProxyforCollectionDto;
import org.folio.modusers.mapper.ProxyForMapper;
import org.folio.modusers.repository.ProxyForRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProxyForService {

  private final ProxyForRepository proxyForRepository;

  private final ProxyForMapper proxyForMapper;

  public ProxyforCollectionDto getProxyFors() {
    return proxyForMapper.mapToProxyForDataCollectionDto(proxyForRepository.findAll());
  }

  public ProxyForDto getProxyForById(final String proxyForId) {
    return proxyForRepository.findById(UUID.fromString(proxyForId))
        .map(proxyForMapper::mapEntityToDto)
        .orElseThrow(() -> new IllegalArgumentException("Proxy for not found"));
  }

  public void deleteProxyFor(final String proxyForId) {
    proxyForRepository.deleteById(UUID.fromString(proxyForId));
  }

  public ProxyForDto saveProxyFor(final ProxyForDto proxyForDto) {
    ProxyFor proxyFor = proxyForMapper.mapDtoToEntity(proxyForDto);
    return proxyForMapper.mapEntityToDto(proxyForRepository.save(proxyFor));
  }

  public void updateProxyFor(final ProxyForDto proxyForDto, final String proxyForId) {
    ProxyFor proxyFor = proxyForRepository.getOne(UUID.fromString(proxyForId));
    proxyForMapper.mapEntityToDto(proxyForDto, proxyFor);
    proxyForRepository.save(proxyFor);
  }
}
