package com.silchenko.arsen.testexampleforclearsolutions.controller;

import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequest;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequest;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;
import com.silchenko.arsen.testexampleforclearsolutions.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateFields(@PathVariable Integer id, @RequestBody UpdateUserRequest request) {
        return userService.updateFields(id, request);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Integer id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }

    @GetMapping("/search")
    public List<UserResponseDto> searchByBirthDate(@RequestParam LocalDate fromDate,
                                                   @RequestParam LocalDate toDate) {
        return userService.searchByBirthDate(fromDate, toDate);
    }
}
