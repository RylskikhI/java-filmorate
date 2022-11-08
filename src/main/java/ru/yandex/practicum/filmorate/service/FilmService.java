package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikesDao likesDao;

    @Autowired
    public FilmService(FilmStorage filmStorage, LikesDao likesDao) {
        this.filmStorage = filmStorage;
        this.likesDao = likesDao;
    }

    public Film addFilm(Film film) {
        log.info("Создание фильма: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обновление фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.findAllFilms();
    }

    public Film getFilm(long id) {
        log.info("Получение фильма {}", id);
        return filmStorage.get(id);
    }

    public void addLike(long filmId, long userId) {
        likesDao.addLikes(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        likesDao.removeLikes(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = filmStorage.getPopularFilms(count);
        if (popularFilms.isEmpty()) {
            return filmStorage.findAllFilms().stream()
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return popularFilms;
        }
    }
}