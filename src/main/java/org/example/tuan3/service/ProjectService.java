package org.example.tuan3.service;

import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.CreateProjectRequest;
import org.example.tuan3.dto.request.UpdateProjectRequest;
import org.example.tuan3.dto.response.ProjectResponse;
import org.example.tuan3.entity.ProjectEntity;
import org.example.tuan3.entity.UserEntity;
import org.example.tuan3.enums.ProjectStatus;
import org.example.tuan3.exception.DuplicateResourceException;
import org.example.tuan3.exception.ResourceNotFoundException;
import org.example.tuan3.repository.ProjectRepository;
import org.example.tuan3.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectResponse createProject(CreateProjectRequest request) {
        String projectName = request.getName().trim();

        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getOwnerId()));

        boolean isDuplicate = projectRepository.existsByNameIgnoreCaseAndOwner_Id(projectName, request.getOwnerId());
        if (isDuplicate) {
            throw new DuplicateResourceException("Project name already exists for this user");
        }

        ProjectEntity project = new ProjectEntity();
        project.setName(projectName);
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus() != null ? request.getStatus() : ProjectStatus.PLANNING);
        project.setOwner(owner);

        ProjectEntity savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Integer id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        return mapToResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return projectRepository.findByOwner_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProjectResponse updateProject(Integer id, UpdateProjectRequest request) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        Integer finalOwnerId = project.getOwner().getId();
        if (request.getOwnerId() != null) {
            finalOwnerId = request.getOwnerId();
        }

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            String newName = request.getName().trim();

            boolean isDuplicate = projectRepository.existsByNameIgnoreCaseAndOwner_IdAndIdNot(
                    newName, finalOwnerId, id
            );

            if (isDuplicate) {
                throw new DuplicateResourceException("Project name already exists for this user");
            }

            project.setName(newName);
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        if (request.getOwnerId() != null && !request.getOwnerId().equals(project.getOwner().getId())) {
            UserEntity newOwner = userRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getOwnerId()));

            project.setOwner(newOwner);
        }

        ProjectEntity updatedProject = projectRepository.save(project);
        return mapToResponse(updatedProject);
    }

    public void deleteProject(Integer id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        projectRepository.delete(project);
    }

    private ProjectResponse mapToResponse(ProjectEntity project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus());
        response.setOwnerId(project.getOwner().getId());
        response.setOwnerName(project.getOwner().getFullName());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }
}