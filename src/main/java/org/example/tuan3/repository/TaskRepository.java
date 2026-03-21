package org.example.tuan3.repository;

import org.example.tuan3.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    List<TaskEntity> findByProject_Id(Integer projectId);

    List<TaskEntity> findByAssignee_Id(Integer userId);
}