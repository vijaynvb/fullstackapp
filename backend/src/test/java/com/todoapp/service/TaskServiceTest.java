package com.todoapp.service;

import com.todoapp.domain.entity.Task;
import com.todoapp.domain.entity.User;
import com.todoapp.domain.enums.TaskPriority;
import com.todoapp.domain.enums.TaskStatus;
import com.todoapp.domain.enums.UserRole;
import com.todoapp.dto.CreateTaskRequest;
import com.todoapp.dto.TaskDTO;
import com.todoapp.mapper.TaskMapper;
import com.todoapp.repository.TaskRepository;
import com.todoapp.repository.UserRepository;
import com.todoapp.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id("user-1")
            .username("testuser")
            .email("test@example.com")
            .firstName("Test")
            .lastName("User")
            .role(UserRole.USER)
            .active(true)
            .build();

        testTask = Task.builder()
            .id("task-1")
            .title("Test Task")
            .description("Test Description")
            .status(TaskStatus.TO_DO)
            .priority(TaskPriority.MEDIUM)
            .createdBy(testUser)
            .build();
    }

    @Test
    void testCreateTask_Success() {
        // Given
        CreateTaskRequest request = CreateTaskRequest.builder()
            .title("New Task")
            .description("New Description")
            .build();

        Task savedTask = Task.builder()
            .id("task-2")
            .title("New Task")
            .description("New Description")
            .status(TaskStatus.TO_DO)
            .priority(TaskPriority.MEDIUM)
            .createdBy(testUser)
            .build();

        TaskDTO taskDTO = TaskDTO.builder()
            .id("task-2")
            .title("New Task")
            .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toDTO(savedTask)).thenReturn(taskDTO);

        // When
        TaskDTO result = taskService.createTask(request, testUser);

        // Then
        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTaskById_NotFound() {
        // Given
        when(taskRepository.findById("non-existent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(com.todoapp.exception.NotFoundException.class, () -> {
            taskService.getTaskById("non-existent", testUser);
        });
    }

    // TODO: Add more test cases
}
