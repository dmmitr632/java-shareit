package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
public class BookingShortDto {
    Integer id;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime start;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime end;
    Integer itemId;
    Integer bookerId;
    BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingShortDto)) {
            return false;
        }

        BookingShortDto that = (BookingShortDto) o;

        if (!Objects.equals(start, that.start)) {
            return false;
        }
        if (!Objects.equals(end, that.end)) {
            return false;
        }
        if (!Objects.equals(itemId, that.itemId)) {
            return false;
        }
        return Objects.equals(bookerId, that.bookerId);
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
        result = 31 * result + (bookerId != null ? bookerId.hashCode() : 0);
        return result;
    }
}
