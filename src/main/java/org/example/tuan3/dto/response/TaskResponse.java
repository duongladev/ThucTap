package org.example.tuan3.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.TaskPriority;
import org.example.tuan3.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;

    private Integer projectId;
    private String projectName;

    private Integer assigneeId;
    private String assigneeName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
