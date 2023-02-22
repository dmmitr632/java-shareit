package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@ToString
@Getter
@Setter
public class BookingShort {
    @EqualsAndHashCode.Exclude
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    private Integer bookerId;
}

