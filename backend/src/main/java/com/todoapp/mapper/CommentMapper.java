package com.todoapp.mapper;

import com.todoapp.domain.entity.Comment;
import com.todoapp.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {
    @Mapping(target = "taskId", source = "task.id")
    CommentDTO toDTO(Comment comment);
}
