package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;


@Value
public class ItemLastNextBookingDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String name;
    @NotBlank String description;
    Boolean available;

    BookingShort lastBooking;
    BookingShort nextBooking;
}