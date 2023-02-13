package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

// @Value
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemDto {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private Integer requestId;
    private BookingShort lastBooking;
    private BookingShort nextBooking;
    private List<CommentDto> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDto)) {
            return false;
        }
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id) && Objects.equals(name, itemDto.name) &&
                Objects.equals(description, itemDto.description) &&
                Objects.equals(available, itemDto.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available);
    }
}