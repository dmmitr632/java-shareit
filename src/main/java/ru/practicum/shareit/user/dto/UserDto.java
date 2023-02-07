package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class UserDto {
    @EqualsAndHashCode.Exclude
    int id;
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
}
