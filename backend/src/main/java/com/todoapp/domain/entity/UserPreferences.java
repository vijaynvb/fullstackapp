package com.todoapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;
    
    @Column(nullable = false, length = 50)
    @Builder.Default
    private String timezone = "UTC";
    
    @Column(name = "date_format", nullable = false, length = 20)
    @Builder.Default
    private String dateFormat = "MM/dd/yyyy";
    
    @Column(name = "time_format", nullable = false, length = 10)
    @Builder.Default
    private String timeFormat = "12h";
    
    @Column(nullable = false, length = 10)
    @Builder.Default
    private String language = "en";
    
    @Column(nullable = false, length = 10)
    @Builder.Default
    private String theme = "light";
}
