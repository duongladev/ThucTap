package org.example.tuan3.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "fullName must not be blank")
    @Size(max = 100, message = "fullName must be <= 100 characters")
    private String fullName;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email is invalid")
    @Size(max = 100, message = "email must be <= 100 characters")
    private String email;

    @NotBlank(message = "phone must not be blank")
    @Size(max = 20, message = "phone must be <= 20 characters")
    private String phone;

    @NotBlank(message = "password must not be blank")
    @Size(min = 6, max = 100, message = "password must be from 6 to 100 characters")
    private String password;
}