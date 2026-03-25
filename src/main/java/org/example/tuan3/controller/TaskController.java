package org.example.tuan3.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.AssignTaskRequest;
import org.example.tuan3.dto.request.CreateTaskRequest;
import org.example.tuan3.dto.request.UpdateTaskStatusRequest;
import org.example.tuan3.dto.response.ApiResponse;
import org.example.tuan3.dto.response.TaskResponse;
import org.example.tuan3.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Task created successfully", response));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{taskId}/assign")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTask(
            @PathVariable @Positive(message = "taskId must be greater than 0") Integer taskId,
            @Valid @RequestBody AssignTaskRequest request
    ) {
        TaskResponse response = taskService.assignTask(taskId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Task assigned successfully", response));
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateStatus(
            @PathVariable @Positive(message = "taskId must be greater than 0") Integer taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        TaskResponse response = taskService.updateStatus(taskId, request);
        return ResponseEntity.ok(ApiResponse.success(200, "Task status updated successfully", response));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByProject(
            @PathVariable @Positive(message = "projectId must be greater than 0") Integer projectId
    ) {
        List<TaskResponse> response = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(ApiResponse.success(200, "Get tasks by project successfully", response));
    }

    @PreAuthorize("hasRole('USER') or #userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByUser(
            @PathVariable @Positive(message = "userId must be greater than 0") Integer userId
    ) {
        List<TaskResponse> response = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(200, "Get tasks by user successfully", response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getMyTasks() {
        List<TaskResponse> response = taskService.getMyTasks();
        return ResponseEntity.ok(ApiResponse.success(200, "Get my tasks successfully", response));
    }
}
