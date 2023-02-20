package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter

public class UserRequestDto {
    @NotBlank
    @NotEmpty
    private String name;

    @NotBlank
    @NotEmpty
    @Email
    private String email;
}
