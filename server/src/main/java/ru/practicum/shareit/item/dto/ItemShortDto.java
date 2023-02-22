package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class ItemShortDto {
    @EqualsAndHashCode.Exclude
    Integer id;
    String name;
    Integer ownerId;
    String description;
    Boolean available;
    Integer requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemShortDto that = (ItemShortDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(available, that.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, available);
    }
}