package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private long filmId;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String title;
    @Size(max = 200, message = "Превышена максимальная длина описания")
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность фильма не может быть отрицательной")
    private Long duration;
    private String genre;
    private String rating;
    private Set<Long> likes;
}