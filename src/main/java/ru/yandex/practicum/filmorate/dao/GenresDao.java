package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDao {
    Genre get(long id);
    List<Genre> getAll();
}
