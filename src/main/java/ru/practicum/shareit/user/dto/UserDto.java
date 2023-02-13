package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder
@AllArgsConstructor
public class UserDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
}
