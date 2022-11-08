package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.GenresDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Primary
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final FilmGenreDao filmGenreDao;
    private final GenresDao genresDao;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate, MpaDao mpaDao,
                        FilmGenreDao filmGenreDao, GenresDao genresDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.filmGenreDao = filmGenreDao;
        this.genresDao = genresDao;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films " +
                "(name, description, release_date, duration, mpa, rate)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement ps =
                    connection.prepareStatement(sqlQuery, new String[] {"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            ps.setInt(6, film.getRate());
            return ps;
        }, keyHolder);
        log.info("generated key: {}", keyHolder.getKey());
        film.setId(keyHolder.getKey().longValue());
        addGenresForFilm(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, " +
                "release_date = ?, mpa = ?, rate = ?, duration = ?" +
                "WHERE id = ?";
        int amountOfUpdated = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getMpa().getId(), film.getRate(),
                film.getDuration(), film.getId());
        if (amountOfUpdated != 1) {
            throw new FilmNotFoundException();
        }
        removeGenresForFilm(film.getId());
        addGenresForFilm(film.getId(), film.getGenres());
        return get(film.getId());
    }

    @Override
    public Film get(long filmId) {
        String sqlQuery = "SELECT * FROM films WHERE id = ?;";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery,
                    (resultSet, rowId) -> buildFilm(resultSet), filmId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new FilmNotFoundException();
        }
        return film;
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sqlQuery = "SELECT * FROM films;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildFilm(resultSet));
    }

    @Override
    public void deleteFilm(Film film) {

    }

    private Film buildFilm(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        Set<Genre> genres = filmGenreDao.getByFilmId(id).stream()
                .map(filmGenres -> genresDao.get(filmGenres.getGenreId()))
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        log.info("genres: {}", genres);
        return new Film(id,
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                genres,
                mpaDao.get(resultSet.getLong("mpa")),
                resultSet.getInt("rate"));
    }

    private void addGenresForFilm(Long id, Set<Genre> genres) {
        if (genres == null) {
            return;
        }
        genres.stream()
                .map(genre -> new FilmGenres(id, genre.getId()))
                .forEach(filmGenreDao::create);
    }

    private void removeGenresForFilm(Long id) {
        List<FilmGenres> filmGenres = filmGenreDao.getByFilmId(id);
        filmGenres.forEach(filmGenreDao::remove);
    }

    public List<Film> getPopularFilms(int size) {
        String sqlQuery = "SELECT f.id \n" +
                "FROM films AS f \n" +
                "LEFT JOIN likes AS l ON f.id = l.film_id \n" +
                "GROUP BY f.name \n" +
                "ORDER BY \n" +
                "    CASE WHEN l.film_id IS NULL THEN 1 ELSE 0 END, \n" +
                "    COUNT(*) DESC \n" +
                "LIMIT ?;";
        List<Integer> popularFilmIds = jdbcTemplate.queryForList(sqlQuery, Integer.class, size);
        return popularFilmIds.stream()
                .map(this::get)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
