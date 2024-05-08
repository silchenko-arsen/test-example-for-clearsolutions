package com.silchenko.arsen.testexampleforclearsolutions.service.impl;

import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;
import com.silchenko.arsen.testexampleforclearsolutions.exception.InvalidArgumentException;
import com.silchenko.arsen.testexampleforclearsolutions.exception.ResourceNotFoundException;
import com.silchenko.arsen.testexampleforclearsolutions.mapper.UserMapper;
import com.silchenko.arsen.testexampleforclearsolutions.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(userService, "ageLimit", 18);
    }


    @Test
    void testCreateUserWithValidUser() {
        CreateUserRequestDto createUserRequest = new CreateUserRequestDto(
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 5, 15),
                "123 Street",
                "1234567890"
        );
        User user = new User();
        user.setEmail(createUserRequest.email());
        user.setFirstName(createUserRequest.firstName());
        user.setLastName(createUserRequest.lastName());
        user.setBirthDate(createUserRequest.birthDate());
        user.setAddress(createUserRequest.address());
        user.setPhoneNumber(createUserRequest.phoneNumber());
        UserResponseDto expectedResponse = new UserResponseDto(
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 5, 15),
                "123 Street",
                "1234567890"
        );
        when(userMapper.mapToModel(createUserRequest)).thenReturn(user);
        when(userMapper.mapToResponseDto(user)).thenReturn(expectedResponse);
        UserResponseDto actualResponse = userService.create(createUserRequest);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testCreateUserWithInsufficientAge() {
        CreateUserRequestDto request = new CreateUserRequestDto(
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(2009, 5, 15),
                "123 Street",
                "1234567890"
        );
        assertThrows(InvalidArgumentException.class, () -> userService.create(request));
    }

    @Test
    void testUpdateFieldsWithValidIdAndUser() {
        Integer userId = 1;
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", null);
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        UserResponseDto expectedResponse = new UserResponseDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "9876543210");
        when(userMapper.mapToResponseDto(UserServiceImpl.getUserMap().get(userId))).thenReturn(expectedResponse);
        UserResponseDto updatedUserResponseDto = userService.updateFields(userId, updateUserRequest);
        assertEquals(expectedResponse, updatedUserResponseDto);
    }

    @Test
    void testUpdateFieldsForNonExistingUser() {
        Integer userId = 5;
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "1234567890");
        assertThrows(ResourceNotFoundException.class, () -> userService.updateFields(userId, updateUserRequest));
    }

    @Test
    void testUpdateFieldsWithInsufficientAge() {
        Integer userId = 1;
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(2009, 5, 15), "123 Main St", "1234567890");
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        assertThrows(InvalidArgumentException.class, () -> userService.updateFields(userId, updateUserRequest));
    }

    @Test
    void testUpdateWithValidIdAndUser() {
        Integer userId = 1;
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "1234567890");
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        User newUser = new User();
        newUser.setEmail(updateUserRequest.email());
        newUser.setFirstName(updateUserRequest.firstName());
        newUser.setLastName(updateUserRequest.lastName());
        newUser.setBirthDate(updateUserRequest.birthDate());
        newUser.setAddress(updateUserRequest.address());
        newUser.setPhoneNumber(updateUserRequest.phoneNumber());
        UserResponseDto expectedResponse = new UserResponseDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "1234567890");
        when(userMapper.mapToResponseDto(newUser)).thenReturn(expectedResponse);
        UserResponseDto updatedUserResponseDto = userService.updateFields(userId, updateUserRequest);
        assertEquals(expectedResponse, updatedUserResponseDto);
    }

    @Test
    void testUpdateForNonExistingUser() {
        Integer userId = 1;
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "1234567890");
        assertThrows(ResourceNotFoundException.class, () -> userService.update(userId, updateUserRequest));
    }

    @Test
    void testUpdateWithInsufficientAge() {
        Integer userId = 1;
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(2009, 5, 15), "123 Main St", "1234567890");
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        assertThrows(InvalidArgumentException.class, () -> userService.update(userId, updateUserRequest));
    }

    @Test
    void testDeleteForExistingUser() {
        Integer id = 1;
        User user = new User();
        UserServiceImpl.getUserMap().put(id, user);
        userService.delete(id);
        assertFalse(UserServiceImpl.getUserMap().containsKey(id));
    }

    @Test
    void testSearchByBirthDateUsersFoundInDateRange() {
        LocalDate fromDate = LocalDate.of(1985, 1, 1);
        LocalDate toDate = LocalDate.of(1995, 12, 31);
        User user1 = new User();
        user1.setEmail("john@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(1990, 10, 20));
        user1.setAddress("123 Main St");
        user1.setPhoneNumber("1234567890");
        User user2 = new User();
        user2.setEmail("john@example.com");
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setBirthDate(LocalDate.of(1987, 10, 20));
        user2.setAddress("123 Main St");
        user2.setPhoneNumber("1234567890");
        UserServiceImpl.getUserMap().put(1, user1);
        UserServiceImpl.getUserMap().put(2, user2);
        UserResponseDto expectedResponse1 = new UserResponseDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 10, 20), "123 Main St", "1234567890");
        UserResponseDto expectedResponse2 = new UserResponseDto("john@example.com", "John", "Doe",
                LocalDate.of(1987, 10, 20), "123 Main St", "1234567890");
        when(userMapper.mapToResponseDto(user1)).thenReturn(expectedResponse1);
        when(userMapper.mapToResponseDto(user2)).thenReturn(expectedResponse2);
        List<UserResponseDto> usersInRange = userService.searchByBirthDate(fromDate, toDate);
        assertEquals(expectedResponse1, usersInRange.get(0));
        assertEquals(expectedResponse2, usersInRange.get(1));
    }

    @Test
    void testSearchByBirthDateUsersNotFoundInDateRange() {
        LocalDate fromDate = LocalDate.of(1997, 1, 1);
        LocalDate toDate = LocalDate.of(1998, 12, 31);
        User user1 = new User();
        user1.setEmail("john@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(1990, 10, 20));
        user1.setAddress("123 Main St");
        user1.setPhoneNumber("1234567890");
        User user2 = new User();
        user2.setEmail("john@example.com");
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setBirthDate(LocalDate.of(1987, 10, 20));
        user2.setAddress("123 Main St");
        user2.setPhoneNumber("1234567890");
        UserServiceImpl.getUserMap().put(1, user1);
        UserServiceImpl.getUserMap().put(2, user2);
        List<UserResponseDto> usersInRange = userService.searchByBirthDate(fromDate, toDate);
        assertTrue(usersInRange.isEmpty());
    }
}


