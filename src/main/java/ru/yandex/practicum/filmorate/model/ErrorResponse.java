package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final String error;
    private final String path;

    public ErrorResponse(String error, String path) {
        this.error = error;
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
