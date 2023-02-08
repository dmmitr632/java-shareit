package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndBookerDto;
import ru.practicum.shareit.booking.dto.ItemShort;
import ru.practicum.shareit.booking.dto.UserShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus());
    }

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker,
                bookingDto.getStatus());
    }

    public static BookingWithItemAndBookerDto toBookingWithItemAndBookerDto(Booking booking) {
        UserShort bookerShort = new UserShort(booking.getBooker().getId());
        ItemShort itemShort = new ItemShort(booking.getItem().getId(), booking.getItem().getName());
        return new BookingWithItemAndBookerDto(booking.getId(),
                booking.getStart(), booking.getEnd(), itemShort, bookerShort, booking.getStatus());
    }

    public static BookingShort toBookingShort(Booking booking) {
        return new BookingShort(booking.getId(), booking.getStart(), booking.getEnd(), booking.getBooker().getId());
    }

}
