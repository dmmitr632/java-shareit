package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class RequestDtoTest {
    @Autowired
    JacksonTester<RequestDto> jacksonTester;
    private final LocalDateTime week = LocalDateTime.of(2024, 2, 15, 12, 0, 15);

    @Test
    void requestDto() throws Exception {
        RequestDto requestDto = RequestDto.builder().id(1).description("description").build();
        JsonContent<RequestDto> jsonContent = jacksonTester.write(requestDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

    @Test
    void requestDtoMethods() {
        RequestDto requestDto1 = new RequestDto(1, "description", week, null);
        RequestDto requestDto2 = new RequestDto(1, "description", week, null);
        assertEquals(requestDto1, requestDto2);
    }

}
