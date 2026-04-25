package com.leonardo.delivery_tracking_system.service;

import com.leonardo.delivery_tracking_system.dto.user.LoginRequest;
import com.leonardo.delivery_tracking_system.dto.user.LoginResponse;
import com.leonardo.delivery_tracking_system.dto.user.RegisterRequest;
import com.leonardo.delivery_tracking_system.enums.UserRole;
import com.leonardo.delivery_tracking_system.exception.EntityAlreadyRegisteredException;
import com.leonardo.delivery_tracking_system.model.User;
import com.leonardo.delivery_tracking_system.repository.UserRepository;
import com.leonardo.delivery_tracking_system.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authManager, TokenService tokenService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request){
        var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = authManager.authenticate(authToken);
        var user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);
        return new LoginResponse(token);
    }

    public void register(RegisterRequest request){
        if(userRepository.findByEmail(request.email()).isPresent())
            throw new EntityAlreadyRegisteredException("Email already registered");
        String hashed = passwordEncoder.encode(request.password());
        User user = new User(request.email(), hashed, UserRole.USER);
        userRepository.save(user);
    }
}
