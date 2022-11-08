package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;


public interface FilmStorage {

    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film get(long filmId);
    Collection<Film> findAllFilms();
    void deleteFilm(Film film);
    List<Film> getPopularFilms(int size);


}
