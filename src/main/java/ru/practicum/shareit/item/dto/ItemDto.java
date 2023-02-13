package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

// @Value
@Builder
@AllArgsConstructor
@Getter
@Setter
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