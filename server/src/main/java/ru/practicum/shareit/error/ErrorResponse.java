package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse extends RuntimeException {
    private final String error;
}