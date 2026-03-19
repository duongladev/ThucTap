package org.example.tuan3.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(max = 100, message = "Full name must be <= 100 characters")
    private String fullName;

    @Email(message = "Email is invalid")
    @Size(max = 100, message = "Email must be <= 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9]{9,15}$", message = "Phone must be 9-15 digits")
    private String phone;

    private Boolean active;
}
