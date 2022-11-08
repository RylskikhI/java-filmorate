package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.exception.FriendshipNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FriendshipDaoImpl implements FriendshipDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(long activeUserId, long passiveUserId) {
        String sqlQuery = "INSERT INTO friendship (active_user_id, passive_user_id) " +
                "VALUES (?, ?);";
        try {
            int amountOfInserted = jdbcTemplate.update(sqlQuery, activeUserId, passiveUserId);
            if (amountOfInserted != 1) {
                throw new UserNotFoundException();
            }
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean isAccepted(long activeUserId, long passiveUserId) {
        String sqlQuery = "SELECT is_accepted FROM friendship " +
                "WHERE active_user_id = ? AND passive_user_id = ?;";
        Boolean isAccepted = jdbcTemplate.queryForObject(sqlQuery, Boolean.class,
                activeUserId, passiveUserId);
        if (isAccepted == null) {
            throw new FriendshipNotFoundException();
        }
        return isAccepted;
    }

    @Override
    public void update(long activeUserId, long passiveUserId, boolean isAccepted) {
        String sqlQuery = "UPDATE friendship SET is_accepted = ? " +
                "WHERE active_user_id = ? AND passive_user_id = ?;";
        try {
            int amountOfUpdated = jdbcTemplate.update(sqlQuery, isAccepted, activeUserId, passiveUserId);
            if (amountOfUpdated != 1) {
                throw new FriendshipNotFoundException();
            }
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void remove(long activeUserId, long passiveUserId) {
        String sqlQuery = "DELETE FROM friendship " +
                "WHERE active_user_id = ? AND passive_user_id = ?;";
        try {
            int amountOfDeleted = jdbcTemplate.update(sqlQuery, activeUserId, passiveUserId);
            if (amountOfDeleted != 1) {
                throw new FriendshipNotFoundException();
            }
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Friendship get(long activeUserId, long passiveUserId) {
        String sqlQuery = "SELECT * FROM friendship " +
                "WHERE active_user_id = ? AND passive_user_id = ?;";
        Friendship friendship;
        try {
            friendship = jdbcTemplate.queryForObject(sqlQuery,
                    (resultSet, rowId) -> buildFriendship(resultSet), activeUserId, passiveUserId);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
        return friendship;
    }

    @Override
    public List<Friendship> getByUserId(long userId) {
        String sqlQuery = "SELECT * FROM friendship " +
                "WHERE active_user_id = ? OR passive_user_id = ?;";
        List<Friendship> friendships = jdbcTemplate.query(sqlQuery,
                (resultSet, rowId) -> buildFriendship(resultSet), userId, userId);
        return friendships;
    }

    public Friendship buildFriendship(ResultSet resultSet) throws SQLException {
        return new Friendship(resultSet.getLong("active_user_id"),
                resultSet.getLong("passive_user_id"),
                resultSet.getBoolean("is_accepted"));
    }
}
