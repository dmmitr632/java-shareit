package ru.practicum.shareit.error;

public class ErrorResponse {
    private String error;
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public ErrorResponse(String description) {
        this.description = description;
    }

    public String getError() {
        return description;
    }
}
