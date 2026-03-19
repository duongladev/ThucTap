package org.example.tuan3.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private Boolean active;
    private LocalDateTime createdAt;
}
