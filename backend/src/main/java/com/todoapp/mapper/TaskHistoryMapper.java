package com.todoapp.mapper;

import com.todoapp.domain.entity.TaskHistory;
import com.todoapp.dto.TaskHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskHistoryMapper {
    @Mapping(target = "taskId", source = "taskId")
    TaskHistoryDTO toDTO(TaskHistory history);
}
