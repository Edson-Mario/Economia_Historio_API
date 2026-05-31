package com.api.ecnomia_api.repository;

import com.api.ecnomia_api.entity.User;
import com.api.ecnomia_api.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByTiposContaining(UserType tipo);
}
