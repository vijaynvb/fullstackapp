package com.todoapp.controller;

import com.todoapp.dto.HealthStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Slf4j
public class HealthController {
    private final DataSource dataSource;

    @GetMapping
    public ResponseEntity<HealthStatus> healthCheck() {
        log.debug("Health check requested");
        
        boolean dbStatus = checkDatabase();
        
        HealthStatus health = HealthStatus.builder()
            .status(dbStatus ? "UP" : "DOWN")
            .database(HealthStatus.DatabaseStatus.builder()
                .status(dbStatus ? "UP" : "DOWN")
                .build())
            .timestamp(LocalDateTime.now())
            .build();
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/readiness")
    public ResponseEntity<String> readinessCheck() {
        log.debug("Readiness check requested");
        boolean dbStatus = checkDatabase();
        if (dbStatus) {
            return ResponseEntity.ok("Ready");
        }
        return ResponseEntity.status(503).body("Not Ready");
    }

    @GetMapping("/liveness")
    public ResponseEntity<String> livenessCheck() {
        log.debug("Liveness check requested");
        return ResponseEntity.ok("Alive");
    }

    private boolean checkDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1);
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return false;
        }
    }
}
