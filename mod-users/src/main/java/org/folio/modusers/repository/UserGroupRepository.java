package org.folio.modusers.repository;

import java.util.UUID;
import org.folio.modusers.domain.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

}
