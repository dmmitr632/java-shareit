package ru.practicum.shareit.item.dto;

/**
 * TODO Sprint add-controllers.
 */
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    @NotNull
    private Integer owner;
    private Integer request;
}