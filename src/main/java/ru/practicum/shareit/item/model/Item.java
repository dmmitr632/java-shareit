package ru.practicum.shareit.item.model;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Item {
    @EqualsAndHashCode.Exclude
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private int owner;
    private Integer request;
}
