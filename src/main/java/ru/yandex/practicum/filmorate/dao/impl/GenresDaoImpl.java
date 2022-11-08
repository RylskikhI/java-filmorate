package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dao.GenresDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenresDaoImpl implements GenresDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre get(long id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?;";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowId) -> buildGenre(resultSet), id);
        } catch(IncorrectResultSizeDataAccessException e) {
            throw new GenreNotFoundException();
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildGenre(resultSet));
    }

    private Genre buildGenre(ResultSet resultSet) throws SQLException {
        return new Genre(resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}
