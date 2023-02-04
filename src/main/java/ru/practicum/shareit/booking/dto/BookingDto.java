package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Value
public class BookingDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime start;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime end;
    @NotNull Item item;
    @NotNull User booker;
    @NotNull BookingStatus status;
}
