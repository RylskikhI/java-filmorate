package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    LocalDate releaseDate = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film addMovie(@RequestBody Film film) {
        log.info("Добавляем фильм {}", film);
        if(film.getName().isBlank() && film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if(film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть меньше нуля или равняться ему.");
        }
        if(film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания фильма не должна превышать 200 символов");
        }
        if(film.getReleaseDate().isBefore(releaseDate)) {
            throw new ValidationException("Дата релиза не должна быть ранее 28 декабря 1895 года");
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateMovie(@RequestBody Film film) {
        log.info("Обновляем фильм {}", film);
        if(film.getId() < 0) {
            throw new ValidationException("Идентификатор фильма не может быть отрицательным");
        }
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllMovies() {
        return films.values();
    }
}
