package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@Primary
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) \n" +
                "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement ps =
                    connection.prepareStatement(sqlQuery, new String[] {"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        log.info("generated key: {}", keyHolder.getKey());
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User getUser(long userId) {
        String sqlQuery = "SELECT id, email, login, name, birthday FROM users \n" +
                "WHERE id = ?;";
        List<User> users = jdbcTemplate.query(sqlQuery,
                (resultSet, rowId) -> buildUser(resultSet), userId);
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        }
        return users.get(0);
    }

    @Override
    public Collection<User> findAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildUser(resultSet));
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users \n" +
                "SET email = ?, login = ?, name = ?, birthday = ? \n" +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return getUser(user.getId());
    }

    @Override
    public void deleteUser(User user) {

    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate());
    }
}
