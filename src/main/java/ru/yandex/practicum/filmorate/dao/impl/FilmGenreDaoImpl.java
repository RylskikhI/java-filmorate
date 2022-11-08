package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(FilmGenres filmGenres) {
        String sqlQuery = "INSERT INTO film_genres (film_id, genre_id)" +
                "VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, filmGenres.getFilmId(), filmGenres.getGenreId());
    }

    @Override
    public void remove(FilmGenres filmGenres) {
        String sqlQuery = "DELETE FROM film_genres " +
                "WHERE film_id = ? AND genre_id = ?;";
        jdbcTemplate.update(sqlQuery, filmGenres.getFilmId(), filmGenres.getGenreId());
    }

    @Override
    public List<FilmGenres> getByFilmId(long filmId) {
        String sqlQuery = "SELECT * FROM film_genres WHERE film_id = ?;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildFilmGenres(resultSet), filmId);
    }

    public FilmGenres buildFilmGenres(ResultSet resultSet) throws SQLException {
        return new FilmGenres(resultSet.getLong("film_id"),
                resultSet.getLong("genre_id"));
    }
}