package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);
    User getUser(long id);
    Collection<User> findAllUsers();
    User updateUser(User user);
    void deleteUser(User user);
}