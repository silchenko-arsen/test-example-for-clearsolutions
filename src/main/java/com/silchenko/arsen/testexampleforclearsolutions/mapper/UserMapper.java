package com.silchenko.arsen.testexampleforclearsolutions.mapper;

import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;
import com.silchenko.arsen.testexampleforclearsolutions.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto mapToResponseDto(User user);

    User mapToModel(CreateUserRequestDto request);

    User mapToModel(UpdateUserRequestDto request);
}
