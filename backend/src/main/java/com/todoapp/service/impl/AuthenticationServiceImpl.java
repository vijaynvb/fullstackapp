package com.todoapp.service.impl;

import com.todoapp.domain.entity.User;
import com.todoapp.domain.enums.AuthType;
import com.todoapp.domain.enums.UserRole;
import com.todoapp.dto.AuthResponse;
import com.todoapp.dto.LoginRequest;
import com.todoapp.dto.SignupRequest;
import com.todoapp.dto.UserDTO;
import com.todoapp.exception.AuthenticationException;
import com.todoapp.exception.ConflictException;
import com.todoapp.exception.NotFoundException;
import com.todoapp.mapper.UserMapper;
import com.todoapp.repository.UserRepository;
import com.todoapp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(SignupRequest request) {
        log.debug("Registering new user with username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered");
        }

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail().trim().toLowerCase())
            .firstName(request.getFirstName().trim())
            .lastName(request.getLastName().trim())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .role(UserRole.USER)
            .active(true)
            .build();

        user = userRepository.save(user);
        log.info("Registered new user: {} (ID: {})", user.getUsername(), user.getId());

        UserDTO userDTO = userMapper.toDTO(user);
        return AuthResponse.builder()
            .user(userDTO)
            .token("session-token-placeholder")
            .expiresAt(LocalDateTime.now().plusHours(8))
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest request) {
        log.debug("Authenticating user with type: {}", request.getType());
        
        User user;
        
        if (request.getType() == AuthType.INTERNAL) {
            String identifier = request.getUsername();
            // Allow login using either username or email
            user = userRepository.findByUsername(identifier)
                    .orElseGet(() -> userRepository.findByEmail(identifier).orElse(null));

            if (user == null) {
                throw new AuthenticationException("Invalid credentials");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new AuthenticationException("Invalid credentials");
            }
        } else if (request.getType() == AuthType.SSO) {
            // TODO: Implement SSO token validation
            throw new UnsupportedOperationException("SSO authentication not yet implemented");
        } else {
            throw new IllegalArgumentException("Invalid authentication type");
        }

        if (!user.isActive()) {
            throw new AuthenticationException("User account is inactive");
        }

        UserDTO userDTO = userMapper.toDTO(user);
        
        // TODO: Create session and generate token
        
        return AuthResponse.builder()
            .user(userDTO)
            .token("session-token-placeholder")
            .expiresAt(LocalDateTime.now().plusHours(8))
            .build();
    }

    @Override
    public void logout(String sessionId) {
        log.debug("Logging out session: {}", sessionId);
        // TODO: Invalidate session
    }

    @Override
    public AuthResponse refreshSession(String sessionId) {
        log.debug("Refreshing session: {}", sessionId);
        // TODO: Validate and refresh session
        throw new UnsupportedOperationException("Session refresh not yet implemented");
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        log.debug("Requesting password reset for email: {}", email);
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found"));
        
        // TODO: Generate reset token and send email
        log.info("Password reset requested for user: {}", user.getId());
    }

    @Override
    @Transactional
    public void confirmPasswordReset(String token, String newPassword) {
        log.debug("Confirming password reset with token");
        
        // TODO: Validate token and update password
        throw new UnsupportedOperationException("Password reset confirmation not yet implemented");
    }
}
