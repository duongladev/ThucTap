package org.example.tuan3.service;

import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.CreateProjectRequest;
import org.example.tuan3.dto.request.UpdateProjectRequest;
import org.example.tuan3.dto.response.ProjectResponse;
import org.example.tuan3.entity.ProjectEntity;
import org.example.tuan3.entity.UserEntity;
import org.example.tuan3.enums.ProjectStatus;
import org.example.tuan3.exception.ResourceNotFoundException;
import org.example.tuan3.repository.ProjectRepository;
import org.example.tuan3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjectResponse getProjectById(Integer id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        return toResponse(project);
    }

    public ProjectResponse createProject(CreateProjectRequest request) {
        UserEntity owner = userRepository.findById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: 1"));

        ProjectEntity project = new ProjectEntity();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus() == null ? ProjectStatus.PLANNING : request.getStatus());
        project.setOwner(owner);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        return toResponse(projectRepository.save(project));
    }

    public ProjectResponse updateProject(Integer id, UpdateProjectRequest request) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }
        project.setUpdatedAt(LocalDateTime.now());

        return toResponse(projectRepository.save(project));
    }

    private ProjectResponse toResponse(ProjectEntity project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus());

        if (project.getOwner() != null) {
            response.setOwnerId(project.getOwner().getId());
            response.setOwnerName(project.getOwner().getFullName());
        }

        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }
}