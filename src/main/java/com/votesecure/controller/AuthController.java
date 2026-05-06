package com.votesecure.controller;

import com.votesecure.dto.request.LoginRequest;
import com.votesecure.dto.response.LoginResponse;
import com.votesecure.model.auth.SystemUser;
import com.votesecure.repository.SystemUserRepository;
import com.votesecure.security.JwtService;
import com.votesecure.service.AuditService;
import com.votesecure.model.audit.AuditLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

@Tag(name = "Authentication", description = "Login for booth officers and election admins")
public class AuthController {

    private final SystemUserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        SystemUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("Account is disabled");
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole().name(),
                user.getBoothId()
        );

        auditService.log(AuditLog.LOGIN, user.getBoothId(), user.getUsername(),
                "User logged in with role: " + user.getRole());

        return ResponseEntity.ok(LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .boothId(user.getBoothId())
                .expiresIn(3600)
                .build());
    }

    public AuthController(SystemUserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuditService auditService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }
}
