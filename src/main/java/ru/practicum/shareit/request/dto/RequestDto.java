package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank @NotNull String description;
    LocalDateTime created;
    Set<ItemShortDto> items;
}
