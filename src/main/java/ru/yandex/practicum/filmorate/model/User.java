package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    @NonNull
    private long id;
    @Email(message = "Некорректный формат email")
    private String email;
    @NonNull
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

}