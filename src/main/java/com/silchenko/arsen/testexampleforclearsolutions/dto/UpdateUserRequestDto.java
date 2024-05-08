package com.silchenko.arsen.testexampleforclearsolutions.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateUserRequestDto(@NotBlank
                                @Email
                                String email,
                                   @NotBlank
                                String firstName,
                                   @NotBlank
                                String lastName,
                                   @NotNull
                                @Past
                                LocalDate birthDate,
                                   String address,
                                   String phoneNumber) {
}
