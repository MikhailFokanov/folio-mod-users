package org.folio.spring.async;

import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class DataFuture<E> {

  private final E data;
  @Getter
  private final CompletableFuture completableFuture;

  public E retrieveData() {
    completableFuture.join();
    return data;
  }
}
