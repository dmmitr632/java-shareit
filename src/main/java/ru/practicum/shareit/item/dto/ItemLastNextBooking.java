package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

public interface ItemLastNextBooking {
    Integer getId();

    String getName();

    String getDescription();

    Boolean getAvailable();

    Integer getLastBookingId();

    Integer getLastBookingBookerId();

    LocalDateTime getLastStart();

    LocalDateTime getLastEnd();

    Integer getNextBookingId();

    Integer getNextBookingBookerId();

    LocalDateTime getNextStart();

    LocalDateTime getNextEnd();
}
