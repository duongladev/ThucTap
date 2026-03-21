package org.example.tuan3.service;

import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.AssignTaskRequest;
import org.example.tuan3.dto.request.CreateTaskRequest;
import org.example.tuan3.dto.request.UpdateTaskStatusRequest;
import org.example.tuan3.dto.response.TaskResponse;
import org.example.tuan3.entity.ProjectEntity;
import org.example.tuan3.entity.TaskEntity;
import org.example.tuan3.entity.UserEntity;
import org.example.tuan3.enums.TaskPriority;
import org.example.tuan3.enums.TaskStatus;
import org.example.tuan3.exception.ResourceNotFoundException;
import org.example.tuan3.repository.ProjectRepository;
import org.example.tuan3.repository.TaskRepository;
import org.example.tuan3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(CreateTaskRequest request) {
        ProjectEntity project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found with id: " + request.getProjectId()));

        TaskEntity task = new TaskEntity();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority() == null ? TaskPriority.MEDIUM : request.getPriority());
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(request.getDueDate());
        task.setProject(project);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        return toResponse(taskRepository.save(task));
    }

    public TaskResponse assignTask(Integer taskId, AssignTaskRequest request) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));

        if (task.getStatus() == TaskStatus.DONE) {
            throw new IllegalArgumentException("Task is DONE, cannot assign");
        }

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getUserId()));

        if (user.getActive() != null && !user.getActive()) {
            throw new IllegalArgumentException("User is inactive, cannot assign task");
        }

        Integer projectId = task.getProject().getId();
        long memberCount = projectRepository.countMemberInProject(projectId, user.getId());
        if (memberCount == 0) {
            throw new IllegalArgumentException("User does not belong to this project");
        }

        task.setAssignee(user);
        task.setUpdatedAt(LocalDateTime.now());

        return toResponse(taskRepository.save(task));
    }

    public TaskResponse updateStatus(Integer taskId, UpdateTaskStatusRequest request) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));

        TaskStatus currentStatus = task.getStatus();
        TaskStatus newStatus = request.getStatus();

        validateStatusTransition(currentStatus, newStatus);

        task.setStatus(newStatus);
        task.setUpdatedAt(LocalDateTime.now());

        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getTasksByProject(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }

        return taskRepository.findByProject_Id(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TaskResponse> getTasksByUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return taskRepository.findByAssignee_Id(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validateStatusTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        if (currentStatus == TaskStatus.DONE) {
            throw new IllegalArgumentException("Task is DONE, status cannot be changed");
        }

        if (currentStatus == newStatus) {
            return;
        }

        if (currentStatus == TaskStatus.TODO && newStatus != TaskStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Task can only move from TODO to IN_PROGRESS");
        }

        if (currentStatus == TaskStatus.IN_PROGRESS && newStatus != TaskStatus.DONE) {
            throw new IllegalArgumentException("Task can only move from IN_PROGRESS to DONE");
        }
    }

    private TaskResponse toResponse(TaskEntity task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());

        if (task.getProject() != null) {
            response.setProjectId(task.getProject().getId());
            response.setProjectName(task.getProject().getName());
        }

        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getId());
            response.setAssigneeName(task.getAssignee().getFullName());
        }

        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        return response;
    }
}
