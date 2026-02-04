package com.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthStatus {
    private String status;
    private DatabaseStatus database;
    private LocalDateTime timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DatabaseStatus {
        private String status;
    }
}
