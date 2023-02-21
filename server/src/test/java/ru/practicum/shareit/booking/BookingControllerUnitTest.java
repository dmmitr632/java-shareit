package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.ItemShort;
import ru.practicum.shareit.booking.dto.UserShort;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.MAX_INTEGER_AS_STRING;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerUnitTest {
    @Autowired
    private ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;

    private BookingShortDto bookingShortDto;

    private final LocalDateTime week = LocalDateTime.of(2023, 3, 14, 22, 0, 15);
    private final LocalDateTime nextWeek = week.plusWeeks(1);

    @BeforeEach
    void init() {
        UserDto bookerDto = UserDto.builder().id(2).name("user 2").email("user2@user.ru").build();

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .build();

        bookingShortDto = BookingShortDto.builder()
                .id(1)
                .start(week)
                .end(nextWeek)
                .itemId(1)
                .bookerId(2)
                .build();

        bookingDto = BookingDto.builder()
                .id(1)
                .start(week)
                .end(nextWeek)
                .booker(toUserShort(bookerDto))
                .item(toItemShort(itemDto))
                .build();
    }

    @Test
    void requestBooking() throws Exception {
        when(bookingService.requestBooking(anyInt(), any())).thenReturn(bookingDto);
        mvc.perform(post("/bookings").content(objectMapper.writeValueAsString(bookingShortDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
        Mockito.verify(bookingService, Mockito.times(1))
                .requestBooking(1, bookingShortDto);
    }

    @Test
    void approveOrRejectBooking() throws Exception {

        when(bookingService.approveOrRejectBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1?approved=true").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
        Mockito.verify(bookingService, Mockito.times(1))
                .approveOrRejectBooking(1, 1, true);
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(anyInt(), anyInt())).thenReturn(bookingDto);
        mvc.perform(get("/bookings/1").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
        Mockito.verify(bookingService, Mockito.times(1))
                .getBookingById(1, 1);
    }

    @Test
    void getBookingByOwnerId() throws Exception {
        when(bookingService.getAllBookingsByOwnerId(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(
                List.of(bookingDto));
        mvc.perform(get("/bookings/owner").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllBookingsByOwnerId(1, "ALL", 0, Integer.valueOf(MAX_INTEGER_AS_STRING));
    }

    @Test
    void getBookingByBookerId() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.getAllBookingsByBookerId(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(
                List.of(bookingDto));
        mvc.perform(get("/bookings").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllBookingsByBookerId(2, "ALL", 0, Integer.valueOf(MAX_INTEGER_AS_STRING));
    }

    public static ItemShort toItemShort(ItemDto itemDto) {
        return new ItemShort(itemDto.getId(), itemDto.getName());
    }

    public static UserShort toUserShort(UserDto bookerDto) {
        return new UserShort(bookerDto.getId());
    }
}

