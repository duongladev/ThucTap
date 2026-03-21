package org.example.tuan3.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.tuan3.enums.ProjectStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponse {
    private Integer id;
    private String name;
    private String description;
    private ProjectStatus status;
    private Integer ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
