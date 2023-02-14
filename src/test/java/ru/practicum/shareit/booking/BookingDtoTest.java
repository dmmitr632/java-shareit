package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    JacksonTester<BookingDto> json;
    private final LocalDateTime week =
            LocalDateTime.of(2024, 2, 15, 12, 0, 15);
    private final LocalDateTime nextWeek = week.plusWeeks(1);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testBookingDto() throws Exception {
        dateTimeFormatter.format(week);
        dateTimeFormatter.format(nextWeek);
        BookingDto bookingDto = BookingDto.builder().id(1).start(week).end(nextWeek).build();
        JsonContent<BookingDto> jsonContent = json.write(bookingDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2024, 2, 15, 12, 0, 15).toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2024, 2, 22, 12, 0, 15).toString());
    }
}