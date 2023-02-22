package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class BookingShortDto {
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
    private Integer itemId;
    private Integer bookerId;
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookingShortDto that = (BookingShortDto) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(bookerId, that.bookerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, itemId, bookerId);
    }
}
