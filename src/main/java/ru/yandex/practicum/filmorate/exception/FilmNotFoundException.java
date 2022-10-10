package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException{

    private final String path;

    public FilmNotFoundException(String message, String path) {
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}