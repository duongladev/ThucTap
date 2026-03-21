package org.example.tuan3.service;

import lombok.RequiredArgsConstructor;
import org.example.tuan3.dto.request.LoginRequest;
import org.example.tuan3.dto.request.RegisterRequest;
import org.example.tuan3.dto.response.AuthResponse;
import org.example.tuan3.entity.RoleEntity;
import org.example.tuan3.entity.UserEntity;
import org.example.tuan3.enums.RoleName;
import org.example.tuan3.exception.DuplicateResourceException;
import org.example.tuan3.exception.ResourceNotFoundException;
import org.example.tuan3.repository.RoleRepository;
import org.example.tuan3.repository.UserRepository;
import org.example.tuan3.security.CustomUserDetails;
import org.example.tuan3.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone already exists");
        }

        RoleEntity userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));

        UserEntity user = new UserEntity();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        return "Register successfully";
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setUserId(userDetails.getId());
        response.setEmail(userDetails.getEmail());
        response.setRoles(userDetails.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .collect(java.util.stream.Collectors.toSet()));

        return response;
    }
}