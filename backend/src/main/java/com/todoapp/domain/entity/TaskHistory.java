package com.todoapp.domain.entity;

import com.todoapp.domain.enums.HistoryAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "task_id", nullable = false)
    private String taskId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HistoryAction action;
    
    @Column(length = 100)
    private String field;
    
    @Column(name = "old_value", length = 500)
    private String oldValue;
    
    @Column(name = "new_value", length = 500)
    private String newValue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by_id", nullable = false)
    private User performedBy;
    
    @CreationTimestamp
    @Column(name = "performed_at", nullable = false, updatable = false)
    private LocalDateTime performedAt;
}
