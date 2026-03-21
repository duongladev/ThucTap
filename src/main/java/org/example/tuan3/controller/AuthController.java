package org.example.tuan3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.LoginRequest;
import org.example.tuan3.dto.request.RegisterRequest;
import org.example.tuan3.dto.response.ApiResponse;
import org.example.tuan3.dto.response.AuthResponse;
import org.example.tuan3.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(201, "Register successfully", authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(200, "Login successfully", authService.login(request))
        );
    }
}