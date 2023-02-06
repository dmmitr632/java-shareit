package ru.practicum.shareit.itemrequest.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class ItemRequestDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String description;
    @NotNull Integer requester;

}
