package org.example.tuan3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.TaskPriority;
import org.example.tuan3.enums.TaskStatus;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    private String description;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private TaskStatus status;

    private LocalDate dueDate;

    @NotNull
    private Long projectId;

    private Long assigneeId;
}
