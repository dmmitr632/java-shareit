package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
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