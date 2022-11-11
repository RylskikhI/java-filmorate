package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikesDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

@Component
public class LikesDaoImpl implements LikesDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLikes(long filmId, long userId) {
        String sqlQuery = "INSERT INTO likes (user_id, film_id) " +
                "VALUES (?, ?);";
        int amountOfUpdated = jdbcTemplate.update(sqlQuery, userId, filmId);
        if (amountOfUpdated != 1) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void removeLikes(long filmId, long userId) {
        String sqlQuery = "DELETE FROM likes " +
                "WHERE user_id = ? AND film_id = ?;";
        int amountOfUpdated = jdbcTemplate.update(sqlQuery, userId, filmId);
        if (amountOfUpdated != 1) {
            throw new UserNotFoundException();
        }
    }
}
