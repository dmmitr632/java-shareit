package ru.practicum.shareit.itemrequest.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequest {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NotBlank
    private String description;
    @NotNull
    private User requester;
}
