package ru.practicum.shareit.booking.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class ItemShort {
    int id;
    @NotNull
    @NotEmpty
    String name;
}
