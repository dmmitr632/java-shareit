package ru.practicum.shareit.item.comment.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class CommentDto {
    Integer id;
    @NotNull
    @NotEmpty
    @NotBlank
    String text;
    String authorName;

    LocalDateTime created;
}
