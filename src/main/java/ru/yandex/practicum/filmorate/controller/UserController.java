package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Создаём пользователя {}", user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (validationUser(user)) {
            userStorage.createUser(user);
            log.info("Добавлен пользователь " + user);
        } else {
            throw new ValidationException("Пользователь не добавлен. Проверьте корректность введенных данных","POST/users");
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обновляем данные пользователя {}", user);
        if (userStorage.getUser(user.getId()) == null) {
            throw new UserNotFoundException("Пользователь с id " + user.getId() + " не найден. Данные не обновлены!");
        }
        if (userStorage.getUser(user.getId()) != null && validationUser(user)) {
            userStorage.updateUser(user);
            log.info("Данные пользователя обновлены " + user);
        } else {
            throw new ValidationException("Проверьте корректность введенных данных","PUT/users");
        }
        return user;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Запрошен список всех пользователей");
        return userStorage.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Запрошен пользователь с id" + id);
        User user = userStorage.getUser(id);
        if (user != null) {
            log.info("Пользователь найден " + user);
            return userStorage.getUser(id);
        } else {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherUserId) {
        log.info("Запрошен список общих друзей у пользователей " + userId + " и " + otherUserId);
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден. ");
        }
        if (otherUser == null) {
            throw new UserNotFoundException("Пользователь с id " + otherUserId + " не найден. ");
        }
        return userService.getUsersCommonFriends(userId, otherUserId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        log.info("Пользователем " + userId + " запрошено добавление в друзья пользователя " + friendId );
            if (user == null) {
                throw new UserNotFoundException("Пользователь с id " + userId + " не найден. ");
            }
            if (friend == null) {
                throw new UserNotFoundException("Пользователь с id " + friendId + " не найден. ");
            }
        userService.addFriend(userId, friendId);
        log.info("Пользователь " + userId + " добавил в друзья " + friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        log.info("Пользователем " + userId + " запрошено удаление из друзей пользователя " + friendId);

            if (user == null) {
                throw new UserNotFoundException("Пользователь с id " + userId + " не найден. ");
            }
            if (friend == null) {
                throw new UserNotFoundException("Пользователь с id " + friendId + " не найден. ");
            }

        userService.deleteFriend(userId, friendId);
        log.info("Пользователь " + userId + " удалил из друзей " + friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable long userId) {
        log.info("Запрошен список друзей пользователя c id " + userId);
        if (userStorage.getUser(userId) == null) {
            throw new UserNotFoundException();
        }
        return userService.getUserFriends(userId);
    }

    private boolean validationUser(User user) {
        boolean isValidated = true;

        if (user == null) {
            throw new UserNotFoundException();
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            isValidated = false;
            log.warn("Некорректный логин");
        }
        if (user.getName() == null) {
            isValidated = false;
            log.warn("Не задано имя пользователя");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            isValidated = false;
            log.warn("Не правильно указана дата рождения: " + user.getBirthday());
        }
        return isValidated;
    }
}