package com.silchenko.arsen.testexampleforclearsolutions.mapper;

import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequest;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequest;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;
import com.silchenko.arsen.testexampleforclearsolutions.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto mapToResponseDto(User user);

    User mapToModel(CreateUserRequest request);

    User mapToModel(UpdateUserRequest request);
}
