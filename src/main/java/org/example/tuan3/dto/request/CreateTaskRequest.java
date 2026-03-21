package org.example.tuan3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.TaskPriority;
import org.example.tuan3.validation.AfterToday;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank(message = "title must not be blank")
    @Size(max = 200, message = "title must be <= 200 characters")
    private String title;

    @Size(max = 1000, message = "description must be <= 1000 characters")
    private String description;

    private TaskPriority priority;

    @AfterToday
    private LocalDate dueDate;

    @NotNull(message = "projectId is required")
    @Positive(message = "projectId must be greater than 0")
    private Integer projectId;
}