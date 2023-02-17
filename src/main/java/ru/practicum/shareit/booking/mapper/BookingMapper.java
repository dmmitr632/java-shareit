package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.ItemShort;
import ru.practicum.shareit.booking.dto.UserShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingShortDto bookingShortDto, User booker, Item item) {
        return new Booking(bookingShortDto.getId(),
                bookingShortDto.getStart(),
                bookingShortDto.getEnd(),
                item,
                booker,
                bookingShortDto.getStatus());
    }

    public static BookingDto toBookingDto(Booking booking) {
        UserShort bookerShort = new UserShort(booking.getBooker().getId());
        ItemShort itemShort = new ItemShort(booking.getItem().getId(), booking.getItem().getName());
        return new BookingDto(booking.getId(),
                booking.getStart(), booking.getEnd(), itemShort, bookerShort, booking.getStatus());
    }

}
