package ru.practicum.shareit.item.comment.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CommentDto {
    Integer id;
    String text;
    String authorName;
    LocalDateTime created;
}
