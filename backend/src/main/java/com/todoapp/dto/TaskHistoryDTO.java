package com.todoapp.dto;

import com.todoapp.domain.enums.HistoryAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskHistoryDTO {
    private String id;
    private String taskId;
    private HistoryAction action;
    private String field;
    private String oldValue;
    private String newValue;
    private UserDTO performedBy;
    private LocalDateTime performedAt;
}
