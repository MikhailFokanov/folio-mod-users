package org.folio.modusers.repository;

import java.util.UUID;
import org.folio.modusers.domain.entity.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType, UUID>
{

}
