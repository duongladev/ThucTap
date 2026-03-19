package org.example.tuan3.repository;

import org.example.tuan3.entity.TaskEntity;
import org.example.tuan3.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByProjectId(Long projectId);
    List<TaskEntity> findByAssigneeId(Long assigneeId);
    List<TaskEntity> findByStatus(TaskStatus status);
}
