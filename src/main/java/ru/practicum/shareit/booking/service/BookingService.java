package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

public interface BookingService {
    BookingDto requestBooking(int userId, BookingShortDto bookingShortDto);

    BookingDto approveOrRejectBooking(int userId, int bookingId, Boolean approvedOrNot);

    BookingDto getBookingById(int userId, int bookingId);

    List<BookingDto> getBookingByBookerId(Integer userId, String state, Integer from, Integer size);

    List<BookingDto> getBookingByOwnerId(Integer userId, String state, Integer from, Integer size);
}
