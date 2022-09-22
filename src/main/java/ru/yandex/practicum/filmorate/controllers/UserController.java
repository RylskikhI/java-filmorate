package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создаём пользователя {}", user);

        /*if (user.getEmail().isBlank() && !user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты не может быть пустым. " +
                    "Адрес электронной почты должен содержать @");
        }*/
        /*if(user.getLogin().isBlank() && user.getLogin().contains(" ")) {
            throw new ValidationException("Login пользователя не может быть пустым и содержать пробелы");
        }*/
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if(user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if(user.getId() < 0) {
            throw new ValidationException("Идентификатор пользователя должен быть больше 0");
        }

        /*if(user.getBirthday().isAfter(today)) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }*/
        users.put(user.getEmail(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обновляем данные пользователя {}", user);
        if(user.getId() <= 0) {
            throw new ValidationException("Идентификатор пользователя должен быть больше нуля");
        }
        users.put(user.getEmail(), user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
