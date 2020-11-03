package org.folio.modusers.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.folio.modusers.dto.ProxyForDto;
import org.folio.modusers.dto.ProxyforCollectionDto;
import org.folio.modusers.proxiesfor.api.ProxiesforApi;
import org.folio.modusers.service.ProxyForService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users-poc")
@RequiredArgsConstructor
public class ProxyForController implements ProxiesforApi {

  private final ProxyForService proxyForService;

  @Override
  public ResponseEntity<Void> deleteProxyForById(String id,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {

    proxyForService.deleteProxyFor(id);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<ProxyforCollectionDto> getProxiesFor(@Valid String query,
      @Min(0) @Max(2147483647) @Valid Integer offset, @Min(0) @Max(2147483647) @Valid Integer limit,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {

    return new ResponseEntity<>(proxyForService.getProxyFors(), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ProxyForDto> getProxyForById(String id,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    return new ResponseEntity<>(proxyForService.getProxyForById(id), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ProxyForDto> postProxiesFor(@Valid ProxyForDto proxyFor,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    return new ResponseEntity<>(proxyForService.saveProxyFor(proxyFor), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> putProxyForById(String id, @Valid ProxyForDto proxyFor,
      @Pattern(regexp = "[a-zA-Z]{2}") @Valid String lang) {
    proxyForService.updateProxyFor(proxyFor, id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
