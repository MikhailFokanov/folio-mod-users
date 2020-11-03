package org.folio.modusers.repository;

import java.util.UUID;
import org.folio.modusers.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID>
{

}
