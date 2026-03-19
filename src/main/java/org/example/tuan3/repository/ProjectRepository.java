package org.example.tuan3.repository;

import org.example.tuan3.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

    List<ProjectEntity> findByOwner_Id(Integer ownerId);

    boolean existsByNameIgnoreCaseAndOwner_Id(String name, Integer ownerId);

    boolean existsByNameIgnoreCaseAndOwner_IdAndIdNot(String name, Integer ownerId, Integer id);
}