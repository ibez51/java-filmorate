package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmValidatorTest {
    @Test
    void shouldReturnHappyPath() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();

        assertEquals("", FilmValidator.validate(film));
    }

    @Test
    void nameMustBeFilled() {
        Film film = Film.builder().name("").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        String estimatedValue = "Название фильма не может быть пустым." + System.lineSeparator();

        assertEquals(estimatedValue, FilmValidator.validate(film));
    }

    @Test
    void descriptionSizeShouldBeLess200() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();

        assertEquals("", FilmValidator.validate(film));

        film.setDescription("Film description Film description Film description Film description Film description Film description Film description Film description Film description Film description Film description Film descript");
        assertEquals("", FilmValidator.validate(film));

        film.setDescription("Film description Film description Film description Film description Film description Film description Film description Film description Film description Film description Film description Film description Film description");
        String estimatedValue = "Описание фильма не может превышать 200 символов." + System.lineSeparator();
        assertEquals(estimatedValue, FilmValidator.validate(film));
    }

    @Test
    void releaseDateShouldBeAfter28121895() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        assertEquals("", FilmValidator.validate(film));

        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertEquals("", FilmValidator.validate(film));

        film.setReleaseDate(LocalDate.of(1885, 12, 28));
        String estimatedValue = "Дата релиза фильма не может быть раньше 28 декабря 1895 года" + System.lineSeparator();
        assertEquals(estimatedValue, FilmValidator.validate(film));
    }

    @Test
    void durationShouldBePositive() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        assertEquals("", FilmValidator.validate(film));

        film.setDuration(0);
        String estimatedValue = "Продолжительность фильма должна быть положительной" + System.lineSeparator();
        assertEquals(estimatedValue, FilmValidator.validate(film));

        film.setDuration(-50);
        assertEquals(estimatedValue, FilmValidator.validate(film));
    }
}