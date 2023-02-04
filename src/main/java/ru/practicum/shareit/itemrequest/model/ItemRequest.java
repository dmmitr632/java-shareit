package ru.practicum.shareit.itemrequest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NotBlank
    private String description;
    @NotNull
    private Integer requester;
}
