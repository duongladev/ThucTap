package org.example.tuan3.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AuthResponse {
    private String token;
    private String tokenType;
    private Integer userId;
    private String email;
    private Set<String> roles;
}