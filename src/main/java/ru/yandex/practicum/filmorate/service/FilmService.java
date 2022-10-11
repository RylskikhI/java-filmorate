package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public void addLike(long filmId, long userId) {
        filmStorage.getFilm(filmId).getLikes().add(userId);
    }

    public void removeLike(long filmId, long userId) {
        filmStorage.getFilm(filmId).getLikes().remove(userId);
    }

    public List<Film> getTopFilms(int topCount) {
        return filmStorage.findAllFilms().stream()
                .sorted((t1, t2) -> t2.getLikes().size() - t1.getLikes().size())
                .limit(topCount)
                .collect(Collectors.toList());
    }
}