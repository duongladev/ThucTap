package org.example.tuan3.repository;

import org.example.tuan3.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

    List<ProjectEntity> findByOwner_Id(Integer ownerId);

    boolean existsByNameIgnoreCaseAndOwner_Id(String name, Integer ownerId);

    boolean existsByNameIgnoreCaseAndOwner_IdAndIdNot(String name, Integer ownerId, Integer id);

    @Query(value = """
        SELECT COUNT(1)
        FROM project_members
        WHERE project_id = :projectId
          AND user_id = :userId
        """, nativeQuery = true)
    long countMemberInProject(@Param("projectId") Integer projectId,
                              @Param("userId") Integer userId);
}