package com.silchenko.arsen.testexampleforclearsolutions.service;

import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequest;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequest;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserResponseDto create(CreateUserRequest request);

    UserResponseDto updateFields(Integer id, UpdateUserRequest request);

    UserResponseDto update(Integer id, UpdateUserRequest request);

    void delete(Integer id);

    List<UserResponseDto> searchByBirthDate(LocalDate fromDate, LocalDate toDate);
}
