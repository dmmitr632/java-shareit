package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Value
public class ItemDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String name;
    @NotBlank String description;
    @NotNull Boolean available;

}