package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotBlank @NotNull String description;
    LocalDateTime created;
    Set<ItemShortDto> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestDto that = (RequestDto) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, created);
    }
}
