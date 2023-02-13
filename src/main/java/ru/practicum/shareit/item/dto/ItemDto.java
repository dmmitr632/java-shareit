package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Value
@Builder
@AllArgsConstructor
public class ItemDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank String name;
    @NotBlank String description;
    Boolean available;
    Integer requestId;
    BookingShort lastBooking;
    BookingShort nextBooking;
    List<CommentDto> comments;
}