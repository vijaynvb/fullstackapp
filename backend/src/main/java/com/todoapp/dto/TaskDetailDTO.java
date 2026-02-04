package com.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetailDTO {
    private TaskDTO task;
    private List<CommentDTO> comments;
    private List<TaskHistoryDTO> history;
}
