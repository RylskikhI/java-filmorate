package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.GenresDao;

import java.util.List;

@Service
public class GenreService {
    private final GenresDao genresDao;

    @Autowired
    public GenreService(GenresDao genresDao) {
        this.genresDao = genresDao;
    }

    public Genre getGenre(long id) {
        return genresDao.get(id);
    }

    public List<Genre> getGenres() {
        return genresDao.getAll();
    }
}