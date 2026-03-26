package org.example.tuan3.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.CreateProjectRequest;
import org.example.tuan3.dto.request.UpdateProjectRequest;
import org.example.tuan3.dto.response.ApiResponse;
import org.example.tuan3.dto.response.ProjectResponse;
import org.example.tuan3.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Validated
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        return ResponseEntity.ok(
                ApiResponse.success(200, "Get all projects successfully", projectService.getAllProjects())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @PathVariable @Positive(message = "id must be greater than 0") Integer id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "Get project successfully", projectService.getProjectById(id))
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody CreateProjectRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(201, "Project created successfully", projectService.createProject(request))
        );
    }
    
}