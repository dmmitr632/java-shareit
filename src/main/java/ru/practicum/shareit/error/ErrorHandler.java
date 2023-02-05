package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongStateException;

import javax.validation.ValidationException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse("400 error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse("404 error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse handleInternalServerErrorException(final WrongStateException e) {
        return new ErrorResponse(e.getMessage());
    }
}

