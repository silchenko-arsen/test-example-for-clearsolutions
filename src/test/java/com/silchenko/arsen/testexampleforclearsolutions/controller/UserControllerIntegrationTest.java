package com.silchenko.arsen.testexampleforclearsolutions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silchenko.arsen.testexampleforclearsolutions.dto.CreateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UpdateUserRequestDto;
import com.silchenko.arsen.testexampleforclearsolutions.dto.UserResponseDto;
import com.silchenko.arsen.testexampleforclearsolutions.model.User;
import com.silchenko.arsen.testexampleforclearsolutions.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @AfterEach
    void afterEach() {
        UserServiceImpl.getUserMap().clear();
    }

    @Test
    void testCreateUserWithValidUser() throws Exception {
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
        UserResponseDto expected = new UserResponseDto(
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 5, 15),
                "123 Street",
                "1234567890"
        );
        String jsonRequest = objectMapper.writeValueAsString(createUserRequest);
        MvcResult result = mockMvc.perform(post("/api/users")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        UserResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    void testCreateUserWithInValidAge() throws Exception {
        CreateUserRequestDto createUserRequest = new CreateUserRequestDto(
                "john@example.com",
                "John",
                "Doe",
                LocalDate.of(2009, 5, 15),
                "123 Street",
                "1234567890"
        );
        String jsonRequest = objectMapper.writeValueAsString(createUserRequest);
        mockMvc.perform(post("/api/users")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateFieldsWithValidIdAndUser() throws Exception {
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
        UserResponseDto expected = new UserResponseDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "9876543210");
        String jsonRequest = objectMapper.writeValueAsString(updateUserRequest);
        MvcResult result = mockMvc.perform(patch("/api/users/{id}", userId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateFieldsForNonExistingUser() throws Exception {
        Integer userId = 1;
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", null);
        String jsonRequest = objectMapper.writeValueAsString(updateUserRequest);
        mockMvc.perform(patch("/api/users/{id}", 2)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFieldsWithInsufficientAge() throws Exception {
        Integer userId = 1;
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(2009, 5, 15), "123 Main St", null);
        String jsonRequest = objectMapper.writeValueAsString(updateUserRequest);
        mockMvc.perform(patch("/api/users/{id}", userId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithValidIdAndUser() throws Exception {
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
        UserResponseDto expected = new UserResponseDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "1234567890");
        String jsonRequest = objectMapper.writeValueAsString(updateUserRequest);
        MvcResult result = mockMvc.perform(put("/api/users/{id}", userId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), UserResponseDto.class);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdateForNonExistingUser() throws Exception {
        Integer userId = 1;
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(1990, 5, 15), "123 Main St", "1234567890");
        String jsonRequest = objectMapper.writeValueAsString(updateUserRequest);
        mockMvc.perform(put("/api/users/{id}", 2)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateWithInsufficientAge() throws Exception {
        Integer userId = 1;
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        UpdateUserRequestDto updateUserRequest = new UpdateUserRequestDto("john@example.com", "John", "Doe",
                LocalDate.of(2009, 5, 15), "123 Main St", "1234567890");
        String jsonRequest = objectMapper.writeValueAsString(updateUserRequest);
        mockMvc.perform(put("/api/users/{id}", userId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteWithValidId() throws Exception {
        Integer userId = 1;
        User user = new User();
        user.setEmail("old@example.com");
        user.setFirstName("Old");
        user.setLastName("User");
        user.setBirthDate(LocalDate.of(1985, 10, 20));
        user.setAddress("456 Oak St");
        user.setPhoneNumber("9876543210");
        UserServiceImpl.getUserMap().put(userId, user);
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
        assertTrue(UserServiceImpl.getUserMap().isEmpty());
    }

    @Test
    void testSearchByBirthDateUsersFoundInDateRange() throws Exception {
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
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/users/search")
                                .param("fromDate", String.valueOf(fromDate))
                                .param("toDate", String.valueOf(toDate)))
                .andExpect(status().isOk())
                .andReturn();
        List<UserResponseDto> expected = List.of(expectedResponse1, expectedResponse2);
        UserResponseDto[] actual = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), UserResponseDto[].class);
        assertEquals(expected, List.of(actual));
    }

    @Test
    void testSearchByBirthDateUsersNotFoundInDateRange() throws Exception {
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
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/users/search")
                                .param("fromDate", String.valueOf(fromDate))
                                .param("toDate", String.valueOf(toDate)))
                .andExpect(status().isOk())
                .andReturn();
        UserResponseDto[] actual = objectMapper.readValue(mvcResult
                .getResponse()
                .getContentAsString(), UserResponseDto[].class);
        assertEquals(0, actual.length);
    }
}
