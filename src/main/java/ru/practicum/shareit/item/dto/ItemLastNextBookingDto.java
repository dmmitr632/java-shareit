package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.item.comment.model.Comment;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Value
public class ItemLastNextBookingDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String name;
    @NotBlank String description;
    Boolean available;

    BookingShort lastBooking;
    BookingShort nextBooking;
    List<Comment> comments;
}