package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse extends RuntimeException {
    private final String error;

//    public ErrorResponse(String error) {
//        this.error = error;
//    }

//    public String getError() {
//        return error;
//    }
}
//public class ErrorResponse {
//    private final String description;
//    private String error;
//
//    public ErrorResponse(String error, String description) {
//        this.error = error;
//        this.description = description;
//    }
//
//    public ErrorResponse(String description) {
//        this.description = description;
//    }
//
//    public String getError() {
//        return description;
//    }
//}
