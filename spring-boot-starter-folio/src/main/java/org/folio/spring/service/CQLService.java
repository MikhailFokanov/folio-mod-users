package org.folio.spring.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import lombok.SneakyThrows;
import org.folio.spring.cql.CQL2JPACriteria;
import org.springframework.stereotype.Service;

@Service
public class CQLService {

  @PersistenceContext
  private EntityManager entityManager;

  @SneakyThrows
  public <E> List<E> getByCQL(Class<E> entityCls, String cql, int offset, int limit) {
    final CQL2JPACriteria<E> cql2JPACriteria = new CQL2JPACriteria<>(entityCls, entityManager);
    final CriteriaQuery<E> criteria = cql2JPACriteria.toCriteria(cql);
    return entityManager
        .createQuery(criteria)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }

}
