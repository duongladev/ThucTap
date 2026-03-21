package org.example.tuan3.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignTaskRequest {

    @NotNull(message = "userId is required")
    @Positive(message = "userId must be greater than 0")
    private Integer userId;
}