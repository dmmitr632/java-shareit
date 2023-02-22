package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BookingDto {
    @EqualsAndHashCode.Exclude
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
    private ItemShort item;
    private UserShort booker;
    private BookingStatus status;
}
