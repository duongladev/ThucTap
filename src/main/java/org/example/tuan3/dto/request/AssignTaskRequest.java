package org.example.tuan3.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTaskRequest {

    @NotNull(message = "userId is required")
    private Integer userId;
}
