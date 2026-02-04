package com.todoapp.service;

import com.todoapp.dto.AuthResponse;
import com.todoapp.dto.LoginRequest;

public interface AuthenticationService {
    AuthResponse authenticate(LoginRequest request);
    void logout(String sessionId);
    AuthResponse refreshSession(String sessionId);
    void requestPasswordReset(String email);
    void confirmPasswordReset(String token, String newPassword);
}
