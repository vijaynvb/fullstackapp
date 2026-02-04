package com.todoapp.mapper;

import com.todoapp.domain.entity.User;
import com.todoapp.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
}
