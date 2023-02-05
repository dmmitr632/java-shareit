package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;


public interface BookingService {
    Booking requestBooking(int userId, BookingDto bookingDto);
    Booking approveOrRejectBooking(int userId, int bookingId, Boolean approvedOrNot);

    Booking getBookingById(int userId, int bookingId);

    List<Booking> getBookingByBookerId(int userId, String state);

    List<Booking> getBookingByOwnerId(int userId, String state);
}
