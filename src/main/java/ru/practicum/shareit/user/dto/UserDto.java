package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank
    String name;
    @NotBlank
    @Email
    String email;
}
