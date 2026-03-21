package org.example.tuan3.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.CreateUserRequest;
import org.example.tuan3.dto.request.UpdateUserRequest;
import org.example.tuan3.dto.response.ApiResponse;
import org.example.tuan3.dto.response.UserResponse;
import org.example.tuan3.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponse.success(200, "Get all users successfully", userService.getAllUsers())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable @Positive(message = "id must be greater than 0") Integer id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "Get user successfully", userService.getUserById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(201, "User created successfully", userService.createUser(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable @Positive(message = "id must be greater than 0") Integer id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "User updated successfully", userService.updateUser(id, request))
        );
    }
}