package org.example.tuan3.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.ProjectStatus;

@Getter
@Setter
public class UpdateProjectRequest {

    @Size(max = 150, message = "Project name must be <= 150 characters")
    private String name;

    @Size(max = 1000, message = "Description must be <= 1000 characters")
    private String description;

    private ProjectStatus status;

    private Integer ownerId;
}