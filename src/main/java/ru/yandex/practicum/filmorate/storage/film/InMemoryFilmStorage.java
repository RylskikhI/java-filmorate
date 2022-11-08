package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component

@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public Film addFilm(Film film) {
        film.setId(++idGenerator);
        /*film.setLikes(new HashSet<>());*/
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(long filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film updateFilm(Film film) {
        /*if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }*/
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        films.remove(film);
    }

    @Override
    public List<Film> getPopularFilms(int size) {
        return null;
    }
}