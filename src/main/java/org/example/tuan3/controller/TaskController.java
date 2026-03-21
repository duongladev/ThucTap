package org.example.tuan3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.AssignTaskRequest;
import org.example.tuan3.dto.request.CreateTaskRequest;
import org.example.tuan3.dto.request.UpdateTaskStatusRequest;
import org.example.tuan3.dto.response.TaskResponse;
import org.example.tuan3.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @PutMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable Integer taskId,
                                                   @Valid @RequestBody AssignTaskRequest request) {
        return ResponseEntity.ok(taskService.assignTask(taskId, request));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Integer taskId,
                                                     @Valid @RequestBody UpdateTaskStatusRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(taskId, request));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }
}
