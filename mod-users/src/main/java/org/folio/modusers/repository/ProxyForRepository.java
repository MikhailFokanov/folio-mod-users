package org.folio.modusers.repository;

import java.util.UUID;
import org.folio.modusers.domain.entity.ProxyFor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyForRepository extends JpaRepository<ProxyFor, UUID> {

}
