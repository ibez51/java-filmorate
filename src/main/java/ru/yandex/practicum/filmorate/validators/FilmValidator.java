package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class FilmValidator {
    public static void validate(Film film) {

        if (Objects.isNull(film.getName())
                || film.getName().isBlank()) {
            log.warn("Film {} has validation errors.", film);

            throw new FilmValidationException("Название фильма не может быть пустым.");
        }

        if (!Objects.isNull(film.getDescription())
                && film.getDescription().length() > 200) {
            log.warn("Film {} has validation errors.", film);

            throw new FilmValidationException("Описание фильма не может превышать 200 символов.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Film {} has validation errors.", film);

            throw new FilmValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            log.warn("Film {} has validation errors.", film);

            throw new FilmValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
