package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @EqualsAndHashCode.Exclude
    int id;
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
}
