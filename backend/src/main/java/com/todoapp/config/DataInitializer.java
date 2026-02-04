package com.todoapp.config;

import com.todoapp.domain.entity.User;
import com.todoapp.domain.enums.UserRole;
import com.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        // Create admin user if it doesn't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@todoapp.com")
                    .firstName("Admin")
                    .lastName("User")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role(UserRole.ADMIN)
                    .active(true)
                    .build();
            
            userRepository.save(admin);
            log.info("Created admin user: admin / admin123");
        } else {
            log.info("Admin user already exists, skipping creation");
        }

        // Create regular user if it doesn't exist
        if (!userRepository.existsByUsername("user")) {
            User user = User.builder()
                    .username("user")
                    .email("user@todoapp.com")
                    .firstName("Regular")
                    .lastName("User")
                    .passwordHash(passwordEncoder.encode("user123"))
                    .role(UserRole.USER)
                    .active(true)
                    .build();
            
            userRepository.save(user);
            log.info("Created regular user: user / user123");
        } else {
            log.info("Regular user already exists, skipping creation");
        }
        
        log.info("Data initialization completed");
    }
}
