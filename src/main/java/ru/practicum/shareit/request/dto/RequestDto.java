package ru.practicum.shareit.request.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class RequestDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String description;
    @NotNull Integer requesterId;

}
