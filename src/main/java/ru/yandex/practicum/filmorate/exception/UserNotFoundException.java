package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {

    private final String path;

    public UserNotFoundException(String message, String path) {
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}




