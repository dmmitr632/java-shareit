package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime start;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime end;
    @NotNull
    private Item item;
    @NotNull
    private User booker;
    @NotNull
    private BookingStatus status;
}
