package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Value
public class ItemWithLastNextBookingIds {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String name;
    @NotBlank String description;
    Boolean available;

    int lastBookingId;
    int lastBookingBookerId;
    LocalDateTime lastStart;
    LocalDateTime lastEnd;

    int nextBookingId;
    int nextBookingBookerId;
    LocalDateTime nextStart;
    LocalDateTime nextEnd;
}
