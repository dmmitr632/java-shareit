package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> jacksonTester;
    private final LocalDateTime week = LocalDateTime.of(2024, 2, 15, 12, 0, 15);
    private final LocalDateTime nextWeek = week.plusWeeks(1);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void bookingDto() throws Exception {
        dateTimeFormatter.format(week);
        dateTimeFormatter.format(nextWeek);
        BookingDto bookingDto = BookingDto.builder().id(1).start(week).end(nextWeek).build();
        JsonContent<BookingDto> jsonContent = jacksonTester.write(bookingDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2024, 2, 15, 12, 0, 15).toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2024, 2, 22, 12, 0, 15).toString());
    }

    @Test
    void bookingDtoMethods() {
        BookingShortDto bookingShortDto1 = new BookingShortDto(1, week, nextWeek, 1, 1,
                BookingStatus.WAITING);
        BookingShortDto bookingShortDto2 = new BookingShortDto(1, week, nextWeek, 1, 1,
                BookingStatus.WAITING);
        assertEquals(bookingShortDto1, bookingShortDto2);
    }
}