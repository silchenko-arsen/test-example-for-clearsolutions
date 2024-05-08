package com.silchenko.arsen.testexampleforclearsolutions.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class User {
    @Getter
    private static int id = 1;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @Past
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    public void idCounter() {
        id++;
    }

}
