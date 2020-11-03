package org.folio.modusers.client;

import org.folio.modusers.domain.dto.ResultList;
import org.springframework.web.bind.annotation.RequestParam;

public interface QueryableClient<E> {

  ResultList<E> query(@RequestParam("query") String query);
}
