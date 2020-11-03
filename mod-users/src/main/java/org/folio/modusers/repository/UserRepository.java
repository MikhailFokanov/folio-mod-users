package org.folio.modusers.repository;

import java.util.List;
import java.util.UUID;
import org.folio.modusers.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  List<User> findByUsername(String username);

  User findByEmailOrPhoneOrMobilePhoneOrUsername(String email, String phone,
      String mobilePhone, String username);
}
