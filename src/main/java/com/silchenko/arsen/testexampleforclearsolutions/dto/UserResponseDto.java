package com.silchenko.arsen.testexampleforclearsolutions.dto;

import java.time.LocalDate;

public record UserResponseDto(String email,
                              String firstName,
                              String lastName,
                              LocalDate birthDate,
                              String address,
                              String phoneNumber) {
}
