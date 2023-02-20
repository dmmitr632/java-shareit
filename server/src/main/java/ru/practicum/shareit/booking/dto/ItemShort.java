package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
@Builder
@AllArgsConstructor
public class ItemShort {
    int id;
    @NotNull
    @NotEmpty
    String name;
}
