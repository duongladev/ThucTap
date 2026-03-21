package org.example.tuan3.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "email must not be blank")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "password must not be blank")
    private String password;
}