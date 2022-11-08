package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmGenres;

import java.util.List;

public interface FilmGenreDao {
    void create(FilmGenres filmGenres);
    void remove(FilmGenres filmGenres);
    List<FilmGenres> getByFilmId(long filmId);
}