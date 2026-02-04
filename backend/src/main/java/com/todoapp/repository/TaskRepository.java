package com.todoapp.repository;

import com.todoapp.domain.entity.Task;
import com.todoapp.domain.enums.TaskPriority;
import com.todoapp.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    Page<Task> findByPriority(TaskPriority priority, Pageable pageable);
    Page<Task> findByAssigneeId(String assigneeId, Pageable pageable);
    Page<Task> findByCreatedById(String createdById, Pageable pageable);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :now AND :future AND t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    List<Task> findUpcomingTasks(@Param("now") LocalDateTime now, @Param("future") LocalDateTime future);
    
    @Query("SELECT t FROM Task t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:assigneeId IS NULL OR t.assignee.id = :assigneeId) AND " +
           "(:createdById IS NULL OR t.createdBy.id = :createdById) AND " +
           "(:overdue IS NULL OR t.overdue = :overdue)")
    Page<Task> findByFilters(
        @Param("status") TaskStatus status,
        @Param("priority") TaskPriority priority,
        @Param("assigneeId") String assigneeId,
        @Param("createdById") String createdById,
        @Param("overdue") Boolean overdue,
        Pageable pageable
    );
}
