package com.dineflow.service.impl;
import com.dineflow.dto.AuthResponseDTO;
import com.dineflow.dto.LoginRequestDTO;
import com.dineflow.dto.RegisterRequestDTO;
import com.dineflow.enums.Role;
import com.dineflow.entity.User;
import com.dineflow.repository.UserRepository;
import com.dineflow.security.JwtService;
import com.dineflow.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String register(RegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(
                request.getPhoneNumber())) {

            throw new RuntimeException(
                    "Phone number already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.CUSTOMER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public AuthResponseDTO login(
            LoginRequestDTO request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"));

        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        if (!matches) {
            throw new RuntimeException(
                    "Invalid email or password");
        }

        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole().name());
        System.out.println("User Role = " +
                user.getRole().name());

        return new AuthResponseDTO(token);
    }
}