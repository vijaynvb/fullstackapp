package com.todoapp.service;

import com.todoapp.domain.entity.User;
import com.todoapp.dto.CreateTaskRequest;
import com.todoapp.dto.TaskDTO;
import com.todoapp.dto.TaskDetailDTO;
import com.todoapp.dto.TaskPageResponse;
import com.todoapp.dto.UpdateTaskRequest;
import com.todoapp.domain.enums.TaskStatus;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDTO createTask(CreateTaskRequest request, User currentUser);
    TaskDTO updateTask(String taskId, UpdateTaskRequest request, User currentUser);
    void deleteTask(String taskId, User currentUser);
    TaskDTO assignTask(String taskId, String assigneeId, User currentUser, boolean notifyAssignee);
    TaskDTO updateTaskStatus(String taskId, TaskStatus status, User currentUser, boolean notify);
    TaskDetailDTO getTaskById(String taskId, User currentUser);
    TaskPageResponse listTasks(
        TaskStatus status,
        com.todoapp.domain.enums.TaskPriority priority,
        String assigneeId,
        String createdById,
        java.util.List<String> tags,
        Boolean overdue,
        String search,
        String sortBy,
        String sortOrder,
        Pageable pageable,
        User currentUser
    );
}
