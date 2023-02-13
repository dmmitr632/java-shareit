package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.BookingShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextBooking;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemShortDto toItemShortDto(Item item) {
        Integer requestId = item.getRequest() != null ? item.getRequest().getId() : null;
        return ItemShortDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .build();
    }

    public static ItemShortDto toItemShortDto(ItemDto itemDto) {
        Integer requestId = itemDto.getRequestId() != null ? itemDto.getRequestId() : null;
        return ItemShortDto.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .requestId(requestId)
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        Integer requestId = item.getRequest() != null ? item.getRequest().getId() : null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .comments(new ArrayList<>())
                .build();
    }

    public static Item toItem(ItemShortDto itemShortDto, User owner, Request request) {
        return new Item(itemShortDto.getId(),
                itemShortDto.getName(),
                itemShortDto.getDescription(),
                itemShortDto.getAvailable(),
                owner,
                request);
    }

    public static ItemDto toItemLastNextBookingDtoComments(ItemLastNextBooking item, List<Comment> comments) {
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

        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null,
                lastBooking, nextBooking, commentDtos);
    }

    public static ItemDto toItemLastNextBookingDtoCommentsDto(ItemLastNextBooking item,
                                                              List<CommentDto> comments) {
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

        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), null,
                lastBooking, nextBooking, comments);
    }

}
