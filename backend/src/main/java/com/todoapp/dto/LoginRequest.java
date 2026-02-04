package com.todoapp.dto;

import com.todoapp.domain.enums.AuthType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotNull(message = "Auth type is required")
    private AuthType type;
    
    private String username;
    private String password;
    private String ssoToken;
    private Boolean rememberMe = false;
}
