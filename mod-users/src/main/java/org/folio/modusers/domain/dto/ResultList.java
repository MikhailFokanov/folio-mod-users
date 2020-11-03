package org.folio.modusers.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;
import lombok.Data;

@Data
public class ResultList<E> {

  @JsonAlias("total_records")
  private Integer totalRecords;

  @JsonAlias({"permissionUsers", "credentials", "permissionNames", "servicePointsUsers"})
  private List<E> result;


}
