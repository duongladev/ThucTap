package org.example.tuan3.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.TaskStatus;

@Getter
@Setter
public class UpdateTaskStatusRequest {

    @NotNull(message = "status is required")
    private TaskStatus status;
}