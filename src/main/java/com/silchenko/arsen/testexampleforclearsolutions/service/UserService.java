package com.silchenko.arsen.testexampleforclearsolutions.service;

import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserResponseDto create(CreateUserRequestDto request);

    UserResponseDto updateFields(Integer id, UpdateUserRequestDto request);

    UserResponseDto update(Integer id, UpdateUserRequestDto request);

    void delete(Integer id);

    List<UserResponseDto> searchByBirthDate(LocalDate fromDate, LocalDate toDate);
}
