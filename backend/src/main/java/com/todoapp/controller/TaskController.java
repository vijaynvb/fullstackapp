package com.todoapp.controller;

import com.todoapp.domain.entity.User;
import com.todoapp.dto.CreateTaskRequest;
import com.todoapp.dto.TaskDTO;
import com.todoapp.dto.TaskDetailDTO;
import com.todoapp.dto.TaskPageResponse;
import com.todoapp.dto.UpdateTaskRequest;
import com.todoapp.domain.enums.TaskPriority;
import com.todoapp.domain.enums.TaskStatus;
import com.todoapp.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Creating task: {}", request.getTitle());
        TaskDTO task = taskService.createTask(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailDTO> getTask(
            @PathVariable String taskId,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Getting task: {}", taskId);
        TaskDetailDTO task = taskService.getTaskById(taskId, currentUser);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable String taskId,
            @Valid @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Updating task: {}", taskId);
        TaskDTO task = taskService.updateTask(taskId, request, currentUser);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String taskId,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Deleting task: {}", taskId);
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<TaskPageResponse> listTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String assigneeId,
            @RequestParam(required = false) String createdById,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Boolean overdue,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "dueDate") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "25") int size,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Listing tasks for user: {}", currentUser.getId());
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        TaskPageResponse response = taskService.listTasks(
            status, priority, assigneeId, createdById, tags, overdue, search, sortBy, sortOrder, pageable, currentUser
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<TaskDTO> assignTask(
            @PathVariable String taskId,
            @RequestBody AssignTaskRequest request,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Assigning task: {} to user: {}", taskId, request.getAssigneeId());
        TaskDTO task = taskService.assignTask(taskId, request.getAssigneeId(), currentUser, request.isNotifyAssignee());
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable String taskId,
            @RequestBody UpdateStatusRequest request,
            @AuthenticationPrincipal User currentUser) {
        log.debug("Updating task status: {} to {}", taskId, request.getStatus());
        TaskDTO task = taskService.updateTaskStatus(taskId, request.getStatus(), currentUser, request.isNotify());
        return ResponseEntity.ok(task);
    }

    // Inner classes for request DTOs
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AssignTaskRequest {
        private String assigneeId;
        private boolean notifyAssignee = true;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UpdateStatusRequest {
        private TaskStatus status;
        private boolean notify = true;
    }
}
