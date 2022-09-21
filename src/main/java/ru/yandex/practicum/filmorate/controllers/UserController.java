package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<String, User> users = new HashMap<>();

    LocalDate today = LocalDate.now();

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Создаём пользователя {}", user);
        if(user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты не может быть пустым и должен содержать '@'");
        }
        if(user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("login пользователя не может быть пустым и содержать пробелы");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(today)) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        users.put(user.getEmail(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Обновляем данные пользователя {}", user);
        if(user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты не может быть пустым и должен содержать '@'");
        }
        if(user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("login пользователя не может быть пустым и содержать пробелы");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(today)) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        users.put(user.getEmail(), user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
