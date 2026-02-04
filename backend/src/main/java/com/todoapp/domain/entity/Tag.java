package com.todoapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    
    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Task> tasks = new HashSet<>();
    
    public int getUsageCount() {
        return tasks != null ? tasks.size() : 0;
    }
    
    public void incrementUsage() {
        // Usage count is calculated from tasks relationship
    }
    
    public void decrementUsage() {
        // Usage count is calculated from tasks relationship
    }
}
