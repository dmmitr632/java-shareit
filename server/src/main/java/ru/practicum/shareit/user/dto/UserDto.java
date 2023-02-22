package ru.practicum.shareit.user.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto {
    @EqualsAndHashCode.Exclude
    Integer id;

    String name;

    String email;
}
