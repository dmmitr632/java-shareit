package ru.practicum.shareit.item.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CommentDto {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
