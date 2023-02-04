package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;


public interface BookingService {
    Booking requestBooking(int userId, Booking booking);
    Booking approveOrRejectBooking(int userId, int bookingId, Boolean approvedOrNot);
}
