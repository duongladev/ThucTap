package org.example.tuan3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.ProjectStatus;

@Getter
@Setter
public class CreateProjectRequest {

    @NotBlank(message = "Project name must not be blank")
    @Size(max = 150, message = "Project name must be <= 150 characters")
    private String name;

    @Size(max = 1000, message = "Description must be <= 1000 characters")
    private String description;

    private ProjectStatus status;

    @NotNull(message = "Owner id must not be null")
    private Integer ownerId;
}