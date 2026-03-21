package org.example.tuan3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.ProjectStatus;

@Getter
@Setter
public class CreateProjectRequest {

    @NotBlank(message = "name must not be blank")
    @Size(max = 150, message = "name must be <= 150 characters")
    private String name;

    @Size(max = 1000, message = "description must be <= 1000 characters")
    private String description;

    private ProjectStatus status;
}