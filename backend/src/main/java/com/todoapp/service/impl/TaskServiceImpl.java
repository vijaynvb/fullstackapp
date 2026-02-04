package com.todoapp.service.impl;

import com.todoapp.domain.entity.Task;
import com.todoapp.domain.entity.User;
import com.todoapp.dto.CreateTaskRequest;
import com.todoapp.dto.TaskDTO;
import com.todoapp.dto.TaskDetailDTO;
import com.todoapp.dto.TaskPageResponse;
import com.todoapp.dto.UpdateTaskRequest;
import com.todoapp.domain.enums.TaskStatus;
import com.todoapp.domain.enums.UserRole;
import com.todoapp.exception.NotFoundException;
import com.todoapp.exception.AuthorizationException;
import com.todoapp.mapper.CommentMapper;
import com.todoapp.mapper.TaskHistoryMapper;
import com.todoapp.mapper.TaskMapper;
import com.todoapp.repository.CommentRepository;
import com.todoapp.repository.TaskHistoryRepository;
import com.todoapp.repository.TaskRepository;
import com.todoapp.repository.UserRepository;
import com.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;
    private final TaskHistoryMapper taskHistoryMapper;

    @Override
    @Transactional
    public TaskDTO createTask(CreateTaskRequest request, User currentUser) {
        log.debug("Creating task: {} by user: {}", request.getTitle(), currentUser.getId());
        
        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TO_DO)
            .priority(request.getPriority() != null ? request.getPriority() : com.todoapp.domain.enums.TaskPriority.MEDIUM)
            .dueDate(request.getDueDate())
            .createdBy(currentUser)
            .build();

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new NotFoundException("Assignee not found"));
            task.assign(assignee);
        }

        Task savedTask = taskRepository.save(task);
        log.info("Task created: {} by user: {}", savedTask.getId(), currentUser.getId());
        
        return taskMapper.toDTO(savedTask);
    }

    @Override
    @Transactional
    public TaskDTO updateTask(String taskId, UpdateTaskRequest request, User currentUser) {
        log.debug("Updating task: {} by user: {}", taskId, currentUser.getId());
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task not found"));

        // Check permissions
        if (!canModifyTask(task, currentUser)) {
            throw new AuthorizationException("You do not have permission to update this task");
        }

        // Update fields
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.updateStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new NotFoundException("Assignee not found"));
            task.assign(assignee);
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated: {} by user: {}", taskId, currentUser.getId());
        
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(String taskId, User currentUser) {
        log.debug("Deleting task: {} by user: {}", taskId, currentUser.getId());
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!canModifyTask(task, currentUser)) {
            throw new AuthorizationException("You do not have permission to delete this task");
        }

        taskRepository.delete(task);
        log.info("Task deleted: {} by user: {}", taskId, currentUser.getId());
    }

    @Override
    @Transactional
    public TaskDTO assignTask(String taskId, String assigneeId, User currentUser, boolean notifyAssignee) {
        log.debug("Assigning task: {} to user: {} by user: {}", taskId, assigneeId, currentUser.getId());
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task not found"));

        if (!canAssignTask(task, currentUser)) {
            throw new AuthorizationException("You do not have permission to assign this task");
        }

        User assignee = userRepository.findById(assigneeId)
            .orElseThrow(() -> new NotFoundException("Assignee not found"));

        task.assign(assignee);
        Task updatedTask = taskRepository.save(task);
        
        // TODO: Send notification if notifyAssignee is true
        
        log.info("Task assigned: {} to user: {} by user: {}", taskId, assigneeId, currentUser.getId());
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    @Transactional
    public TaskDTO updateTaskStatus(String taskId, TaskStatus status, User currentUser, boolean notify) {
        log.debug("Updating task status: {} to {} by user: {}", taskId, status, currentUser.getId());
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task not found"));

        // TODO: Validate state transition
        
        task.updateStatus(status);
        
        // Update overdue flag
        if (status == TaskStatus.COMPLETED) {
            task.clearOverdue();
        } else if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            task.markOverdue();
        }

        Task updatedTask = taskRepository.save(task);
        
        // TODO: Send notification if notify is true
        
        log.info("Task status updated: {} to {} by user: {}", taskId, status, currentUser.getId());
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDetailDTO getTaskById(String taskId, User currentUser) {
        log.debug("Getting task: {} by user: {}", taskId, currentUser.getId());
        
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task not found"));

        // TODO: Check if user has access to this task
        
        TaskDTO taskDTO = taskMapper.toDTO(task);
        var comments = commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId)
            .stream()
            .map(commentMapper::toDTO)
            .toList();
        var history = taskHistoryRepository.findByTaskIdOrderByPerformedAtDesc(taskId)
            .stream()
            .map(taskHistoryMapper::toDTO)
            .toList();
        
        return TaskDetailDTO.builder()
            .task(taskDTO)
            .comments(comments)
            .history(history)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskPageResponse listTasks(
        TaskStatus status,
        com.todoapp.domain.enums.TaskPriority priority,
        String assigneeId,
        String createdById,
        List<String> tags,
        Boolean overdue,
        String search,
        String sortBy,
        String sortOrder,
        Pageable pageable,
        User currentUser
    ) {
        log.debug("Listing tasks for user: {}", currentUser.getId());
        
        // TODO: Apply filters based on user role
        // Users see their own tasks and assigned tasks
        // Managers see team tasks
        
        Page<Task> taskPage = taskRepository.findByFilters(status, priority, assigneeId, createdById, overdue, pageable);
        
        return TaskPageResponse.builder()
            .content(taskPage.getContent().stream().map(taskMapper::toDTO).toList())
            .page(taskPage.getNumber())
            .size(taskPage.getSize())
            .totalElements(taskPage.getTotalElements())
            .totalPages(taskPage.getTotalPages())
            .build();
    }

    private boolean canModifyTask(Task task, User user) {
        return task.getCreatedBy().getId().equals(user.getId()) ||
               user.hasRole(UserRole.MANAGER) ||
               user.hasRole(UserRole.ADMIN);
    }

    private boolean canAssignTask(Task task, User user) {
        return task.getCreatedBy().getId().equals(user.getId()) ||
               user.hasRole(UserRole.MANAGER) ||
               user.hasRole(UserRole.ADMIN);
    }
}
