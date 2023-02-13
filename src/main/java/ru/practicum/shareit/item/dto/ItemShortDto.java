package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@AllArgsConstructor
public class ItemShortDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String name;
    Integer ownerId;
    @NotBlank String description;
    Boolean available;
    Integer requestId;

}