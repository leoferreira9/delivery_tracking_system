package com.leonardo.delivery_tracking_system.controller;

import com.leonardo.delivery_tracking_system.dto.user.LoginRequest;
import com.leonardo.delivery_tracking_system.dto.user.LoginResponse;
import com.leonardo.delivery_tracking_system.dto.user.RegisterRequest;
import com.leonardo.delivery_tracking_system.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Authenticate user and generate JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        log.info("Authenticating user: {} and generating new token", request.email());
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Register a new user account")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request){
        log.info("Registering new user: {}", request.email());
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
