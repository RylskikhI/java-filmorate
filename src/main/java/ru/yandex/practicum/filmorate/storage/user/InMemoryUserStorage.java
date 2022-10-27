package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public User createUser(User user) throws ValidationException {
        user.setUserId(++idGenerator);
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
}