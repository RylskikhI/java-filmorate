package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa get(long id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?;";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowId) -> buildMpa(resultSet), id);
        } catch(IncorrectResultSizeDataAccessException e) {
            throw new MpaNotFoundException();
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM mpa;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildMpa(resultSet));
    }

    private Mpa buildMpa(ResultSet resultSet) throws SQLException {
        return new Mpa(resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}
