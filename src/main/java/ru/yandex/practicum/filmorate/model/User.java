package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import javax.validation.constraints.PositiveOrZero.List;
import java.time.LocalDate;

@Data
public class User {
    private Integer id = 1;
    @Email
    private String email;
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

}
