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
        if (!(o instanceof RequestDto)) {
            return false;
        }

        RequestDto that = (RequestDto) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
