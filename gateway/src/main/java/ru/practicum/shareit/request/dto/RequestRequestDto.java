package ru.practicum.shareit.request.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class RequestRequestDto {
    @NotBlank
    @NotNull
    @NotEmpty
    private String description;
}