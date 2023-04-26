package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Objects;

public class FilmValidator {
    public static String validate(Film film) {
        StringBuilder retBuilder = new StringBuilder();

        if (Objects.isNull(film.getName())
                || film.getName().isBlank()) {
            retBuilder.append("Название фильма не может быть пустым.").append(System.lineSeparator());
        }

        if (!Objects.isNull(film.getDescription())
        && film.getDescription().length() > 200) {
            retBuilder.append("Описание фильма не может превышать 200 символов.").append(System.lineSeparator());
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            retBuilder.append("Дата релиза фильма не может быть раньше 28 декабря 1895 года").append(System.lineSeparator());
        }

        if (film.getDuration() <= 0) {
            retBuilder.append("Продолжительность фильма должна быть положительной").append(System.lineSeparator());
        }

        return retBuilder.toString();
    }
}
