package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/films")

public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final UserStorage userStorage;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавляем фильм {}", film);

        if (validationFilm(film)) {
            filmStorage.addFilm(film);
            log.info("Фильм добавлен");
        } else {
            log.warn("Фильм не добавлен!");
            throw new ValidationException("Проверьте корректность введенных данных", "POST/films");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновляем фильм {}", film);
        if (filmStorage.getFilm(film.getId()) == null) {
            log.warn("Фильм c id " + film.getId() + " не найден. Данные не обновлены");
            throw new FilmNotFoundException("Фильм не найден. Данные не обновлены!", "PUT/films");
        }
        if (validationFilm(film)) {
            filmStorage.updateFilm(film);
            log.info("Фильм обновлен");
        } else {
            log.warn("Фильм не обновлен!");
            throw new ValidationException("Проверьте корректность введенных данных", "PUT/films");
        }
        return film;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Запрошен список всех фильмов");
        return filmStorage.findAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film != null) {
            return filmStorage.getFilm(filmId);
        } else {
            throw new FilmNotFoundException("Фильм с id " + filmId + " не найден", "GET/films/"+filmId);
        }
    }

    @DeleteMapping
    public void deleteFilms(@Valid @RequestBody Film film) {
        filmStorage.deleteFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);

        log.info("Добавляем лайк для фильма " + filmId + " от пользователя " + userId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм c id " + filmId + " не найден!", "PUT/films/" + filmId
                    + "/like/" + userId);
        }
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден", "PUT/films/"
                    + filmId + "/like/" + userId);
        }
        filmService.addLike(filmId, userId);
        log.info("Лайк добавлен");
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);

        log.info("Удаляем лайк у фильма " + filmId + " от пользователя " + userId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм c id " + filmId + " не найден!", "DELETE/films/"
                    + filmId + "/like/" + userId);
        }
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден", "DELETE/films/"
                    + filmId + "/like/" + userId);
        }
        filmService.removeLike(filmId, userId);
        log.info("Лайк удален");
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Запрошен список фильмов с наибольшим количеством лайков");
        return filmService.getTopFilms(count);
    }

    private boolean validationFilm(Film film) {
        boolean isValidated = true;

        if (film == null) {
            log.warn("Фильм не существует!");
            return false;
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            isValidated = false;
            log.warn("Неправильно указана дата выхода фильма: " + film.getReleaseDate());
        }

        return isValidated;
    }
}