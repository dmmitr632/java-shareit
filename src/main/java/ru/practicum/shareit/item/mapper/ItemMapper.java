package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.BookingShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextBooking;
import ru.practicum.shareit.item.dto.ItemLastNextBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }


    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner);
    }

    public static ItemLastNextBookingDto toItemLastNextBookingDto(ItemLastNextBooking item) {
        BookingShort lastBooking;
        BookingShort nextBooking;
        if (item.getLastBookingId() != null) {
            lastBooking = new BookingShort(item.getLastBookingId(), item.getLastStart(), item.getLastEnd(),
                    item.getLastBookingBookerId());
        } else {
            lastBooking = null;
        }
        if (item.getNextBookingId() != null) {
            nextBooking = new BookingShort(item.getNextBookingId(), item.getNextStart(), item.getNextEnd(),
                    item.getNextBookingBookerId());
        } else {
            nextBooking = null;
        }
        return new ItemLastNextBookingDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                lastBooking, nextBooking);
    }

//    public static ItemLastNextBookingDto toItemLastNextBookingDtoFromItem(Item item
//            , BookingShort lastBooking, BookingShort nextBooking) {
//        return new ItemLastNextBookingDto(item.getId(), item.getName(), item.getDescription(),
//                item.getAvailable(), lastBooking, nextBooking);
//    }
}
