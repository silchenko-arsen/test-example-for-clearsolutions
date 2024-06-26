package com.silchenko.arsen.testexampleforclearsolutions.service.impl;

import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;
import com.silchenko.arsen.testexampleforclearsolutions.exception.InvalidArgumentException;
import com.silchenko.arsen.testexampleforclearsolutions.exception.ResourceNotFoundException;
import com.silchenko.arsen.testexampleforclearsolutions.mapper.UserMapper;
import com.silchenko.arsen.testexampleforclearsolutions.model.User;
import com.silchenko.arsen.testexampleforclearsolutions.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${user.age.limit}")
    private Integer ageLimit;
    private final UserMapper userMapper;
    @Getter
    private static final Map<Integer, User> userMap = new HashMap<>();

    @Override
    public UserResponseDto create(CreateUserRequestDto request) {
        validateAge(request.birthDate());
        User user = userMapper.mapToModel(request);
        userMap.put(User.getId(), user);
        user.idCounter();
        return userMapper.mapToResponseDto(user);
    }

    @Override
    public UserResponseDto updateFields(Integer id, UpdateUserRequestDto request) {
        if (userMap.containsKey(id)) {
            User user = userMap.get(id);
            if (request.birthDate() != null) {
                validateAge(request.birthDate());
                user.setBirthDate(request.birthDate());
            }
            if (request.email() != null && !request.email().isEmpty()) {
                user.setEmail(request.email());
            }
            if (request.firstName() != null && !request.firstName().isEmpty()) {
                user.setFirstName(request.firstName());
            }
            if (request.lastName() != null && !request.lastName().isEmpty()) {
                user.setLastName(request.lastName());
            }
            if (request.address() != null && !request.address().isEmpty()) {
                user.setAddress(request.address());
            }
            if (request.phoneNumber() != null && !request.phoneNumber().isEmpty()) {
                user.setPhoneNumber(request.phoneNumber());
            }
            userMap.put(id, user);
            return userMapper.mapToResponseDto(user);
        } else {
            throw new ResourceNotFoundException("User with this id " + id + " did not find.");
        }
    }

    @Override
    public UserResponseDto update(Integer id, UpdateUserRequestDto request) {
        if (userMap.containsKey(id)) {
            validateAge(request.birthDate());
            User user = userMapper.mapToModel(request);
            userMap.put(id, user);
            return userMapper.mapToResponseDto(user);
        } else {
            throw new ResourceNotFoundException("User with this id " + id + " did not find.");
        }
    }

    @Override
    public void delete(Integer id) {
        userMap.remove(id);
    }

    @Override
    public List<UserResponseDto> searchByBirthDate(LocalDate fromDate, LocalDate toDate) {
        List<User> usersInRange = new ArrayList<>();
        for (User user : userMap.values()) {
            if (!user.getBirthDate().isBefore(fromDate) && !user.getBirthDate().isAfter(toDate)) {
                usersInRange.add(user);
            }
        }
        return usersInRange.stream().map(userMapper::mapToResponseDto).toList();
    }

    private void validateAge(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (ageLimit > age) {
            throw new InvalidArgumentException(age + " years is not enough for registration.");
        }
    }
}
