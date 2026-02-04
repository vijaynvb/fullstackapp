package com.todoapp.repository;

import com.todoapp.domain.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, String> {
    List<TaskHistory> findByTaskIdOrderByPerformedAtDesc(String taskId);
}
