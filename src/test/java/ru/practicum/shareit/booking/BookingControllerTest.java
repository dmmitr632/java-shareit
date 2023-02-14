package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;

    private BookingShortDto bookingShortDto;

    @BeforeEach
    void init() {
        UserDto userDto = UserDto.builder().id(2).name("user 1").email("user@user.ru").build(); // booker;

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("item")
                .description("description")
                .available(true)
                .build();

        bookingShortDto = BookingShortDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(1))
                .itemId(1)
                .bookerId(2)
                .build();

        bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusWeeks(1))
                .booker(toUserShort(userDto))
                .item(toItemShort(itemDto))
                .build();
    }

    @Test
    void requestBooking() throws Exception {
        when(bookingService.requestBooking(anyInt(), any())).thenReturn(bookingDto);
        mvc.perform(post("/bookings").content(mapper.writeValueAsString(bookingShortDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void approveOrRejectBooking() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.approveOrRejectBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1?approved=true").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getByIdTest() throws Exception {
        when(bookingService.getBookingById(anyInt(), anyInt())).thenReturn(bookingDto);
        mvc.perform(get("/bookings/1").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getBookingByOwnerId() throws Exception {
        when(bookingService.getBookingByOwnerId(anyInt(), anyString(), anyInt(), eq(null))).thenReturn(
                List.of(bookingDto));
        mvc.perform(get("/bookings/owner").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void getAllByUserTest() throws Exception {
        when(bookingService.getBookingByBookerId(anyInt(), anyString(), anyInt(), eq(null))).thenReturn(
                List.of(bookingDto));
        mvc.perform(get("/bookings").characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }


    public static ItemShort toItemShort(ItemDto itemDto) {
        return new ItemShort(itemDto.getId(), itemDto.getName());
    }

    public static UserShort toUserShort(UserDto userDto) {
        return new UserShort(userDto.getId());
    }

}