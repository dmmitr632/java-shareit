package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.BookingShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextBooking;
import ru.practicum.shareit.item.dto.ItemLastNextBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }


    public static Item toItem(ItemDto itemDto, User owner, Request request) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                request);
    }

    public static ItemLastNextBookingDto toItemLastNextBookingDtoComments(ItemLastNextBooking item, List<Comment> comments) {
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
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(comment -> commentDtos.add(CommentMapper.toCommentDto(comment)));

        return new ItemLastNextBookingDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                lastBooking, nextBooking, commentDtos);
    }

    public static ItemLastNextBookingDto toItemLastNextBookingDtoCommentsDto(ItemLastNextBooking item, List<CommentDto> comments) {
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
                lastBooking, nextBooking, comments);
    }


}
