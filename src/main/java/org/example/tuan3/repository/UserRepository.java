package org.example.tuan3.repository;


import org.example.tuan3.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<UserEntity> findByEmail(String email);
}
