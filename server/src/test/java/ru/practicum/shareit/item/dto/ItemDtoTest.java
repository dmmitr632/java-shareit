package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemDtoTest {
    @Autowired
    private JacksonTester<Object> jacksonTester;
    private final LocalDateTime week = LocalDateTime.of(2024, 2, 14, 12, 0, 15);
    private final LocalDateTime nextWeek = week.plusWeeks(1);

    @Test
    void itemDto() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .available(true)
                .description("description")
                .build();

        JsonContent<Object> jsonContent = jacksonTester.write(itemDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    void bookingShort() throws Exception {
        BookingShort bookingShort = BookingShort.builder()
                .id(1)
                .start(week)
                .end(nextWeek)
                .bookerId(1)
                .build();

        System.out.println(bookingShort);

        JsonContent<Object> jsonContent = jacksonTester.write(bookingShort);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2024, 2, 14, 12, 0, 15).toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2024, 2, 21, 12, 0, 15).toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }

    @Test
    void itemShortDto() throws Exception {
        ItemShortDto itemShortDto = ItemShortDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .requestId(1)
                .available(false)
                .ownerId(123)
                .build();

        JsonContent<Object> jsonContent = jacksonTester.write(itemShortDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isFalse();
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerId").isEqualTo(123);
    }

    @Test
    void itemDtoMethods() {
        ItemDto itemDto1 = new ItemDto(null, "name", "description", true, null, null, null, null);
        ItemDto itemDto2 = new ItemDto(null, "name", "description", true, null, null, null, null);
        assertEquals(itemDto1, itemDto2);
    }

}