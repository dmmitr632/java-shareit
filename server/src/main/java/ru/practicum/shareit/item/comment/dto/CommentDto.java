package ru.practicum.shareit.item.comment.dto;

import lombok.*;

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

    private String text;
    private String authorName;
    private LocalDateTime created;
}
