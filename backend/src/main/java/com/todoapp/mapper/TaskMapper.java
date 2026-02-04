package com.todoapp.mapper;

import com.todoapp.domain.entity.Task;
import com.todoapp.dto.TaskDTO;
import com.todoapp.dto.TaskDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface TaskMapper {
    @Mapping(target = "tags", expression = "java(task.getTags() != null ? task.getTags().stream().map(com.todoapp.domain.entity.Tag::getName).toList() : java.util.Collections.emptyList())")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    TaskDTO toDTO(Task task);

    TaskDetailDTO toDetailDTO(Task task);

    List<TaskDTO> toDTOList(List<Task> tasks);
}
