package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotEmpty @NotBlank
    private final String name;
    private String description;
    private final LocalDate releaseDate;
    @PositiveOrZero
    private final long duration;
}
