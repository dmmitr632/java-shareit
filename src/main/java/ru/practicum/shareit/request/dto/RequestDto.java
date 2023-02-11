package ru.practicum.shareit.request.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public class RequestDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank @NotNull String description;
    LocalDateTime created;
    Set<ItemDto> items;
}
