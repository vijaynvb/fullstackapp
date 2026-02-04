package com.todoapp.controller;

import com.todoapp.dto.AuthResponse;
import com.todoapp.dto.LoginRequest;
import com.todoapp.dto.MessageResponse;
import com.todoapp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        log.debug("Login attempt for type: {}", request.getType());
        AuthResponse authResponse = authenticationService.authenticate(request);
        // TODO: Set session cookie
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest request) {
        log.debug("Logout request");
        // TODO: Extract session ID from request
        authenticationService.logout("session-id");
        return ResponseEntity.ok(new MessageResponse("Logout successful", LocalDateTime.now()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshSession(HttpServletRequest request) {
        log.debug("Session refresh request");
        // TODO: Extract session ID from request
        AuthResponse authResponse = authenticationService.refreshSession("session-id");
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<MessageResponse> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        log.debug("Password reset request for email: {}", request.getEmail());
        authenticationService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("Password reset email sent", LocalDateTime.now()));
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<MessageResponse> confirmPasswordReset(@RequestBody ConfirmPasswordResetRequest request) {
        log.debug("Password reset confirmation");
        authenticationService.confirmPasswordReset(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("Password reset successful", LocalDateTime.now()));
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class PasswordResetRequest {
        private String email;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ConfirmPasswordResetRequest {
        private String token;
        private String newPassword;
    }
}
