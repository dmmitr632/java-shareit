package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


public interface BookingService {
    Booking requestBooking(int userId, Booking booking);
    Booking approveOrRejectBooking(int userId, int bookingId, Boolean approvedOrNot);

    Booking getBookingById(int userId, int bookingId);

    List<BookingDto> getBookingByBookerId(int userId, String state);

    List<BookingDto> getBookingByOwnerId(int userId, String state);
}
