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
        log.info("/films (PUT): {}", film);
        validationFilm(film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Запрошен список всех фильмов");
        return filmStorage.findAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        Film film = filmStorage.get(filmId);
        if (film != null) {
            return filmStorage.get(filmId);
        } else {
            throw new FilmNotFoundException();
        }
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable long filmId) {
        log.info("Запрошено удаление фильма с id " + filmId);
        Film film = filmStorage.get(filmId);
        if (film != null) {
            log.info("Удаляем фильм " + film.getName());
            userStorage.deleteUser(filmId);
        } else {
            throw new FilmNotFoundException(filmId);
        }
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        Film film = filmStorage.get(filmId);
        User user = userStorage.getUser(userId);

        log.info("Добавляем лайк для фильма " + filmId + " от пользователя " + userId);
        if (film == null) {
            throw new FilmNotFoundException();
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        filmService.addLike(filmId, userId);
        log.info("Лайк добавлен");
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        Film film = filmStorage.get(filmId);
        User user = userStorage.getUser(userId);

        log.info("Удаляем лайк у фильма " + filmId + " от пользователя " + userId);
        if (film == null) {
            throw new FilmNotFoundException();
        }
        if (user == null) {
            throw new UserNotFoundException();
        }
        filmService.removeLike(filmId, userId);
        log.info("Лайк удален");
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Запрошен список фильмов с наибольшим количеством лайков");
        return filmService.getPopularFilms(count);
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