package org.example.tuan3.repository;

import org.example.tuan3.entity.RoleEntity;
import org.example.tuan3.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(RoleName name);
}