package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
public class WrongStateException extends RuntimeException {

    public WrongStateException(String message) {
        super(message);
    }


}
