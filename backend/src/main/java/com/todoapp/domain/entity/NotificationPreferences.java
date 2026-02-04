package com.todoapp.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferences {
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;
    
    @Column(name = "email_enabled", nullable = false)
    @Builder.Default
    private Boolean emailEnabled = true;
    
    @Column(name = "task_assigned", nullable = false)
    @Builder.Default
    private Boolean taskAssigned = true;
    
    @Column(name = "task_reassigned", nullable = false)
    @Builder.Default
    private Boolean taskReassigned = true;
    
    @Column(name = "due_date_reminder", nullable = false)
    @Builder.Default
    private Boolean dueDateReminder = true;
    
    @ElementCollection
    @CollectionTable(name = "reminder_days_before", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "days")
    @Builder.Default
    private List<Integer> reminderDaysBefore = new ArrayList<>(List.of(1, 3));
    
    @Column(name = "overdue_notification", nullable = false)
    @Builder.Default
    private Boolean overdueNotification = true;
    
    @Column(name = "overdue_notification_frequency", length = 20)
    @Builder.Default
    private String overdueNotificationFrequency = "daily";
    
    @Column(name = "comment_added", nullable = false)
    @Builder.Default
    private Boolean commentAdded = true;
    
    @Column(name = "status_changed", nullable = false)
    @Builder.Default
    private Boolean statusChanged = false;
}
