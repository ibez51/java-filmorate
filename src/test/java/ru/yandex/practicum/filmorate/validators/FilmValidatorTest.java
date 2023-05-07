package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidatorTest {
    @Test
    void shouldReturnHappyPath() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();

        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void nameMustBeFilled() {
        Film film = Film.builder().name("").description("Film description").releaseDate(LocalDate.now()).duration(50).build();

        assertThrows(FilmValidationException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void descriptionSizeShouldBeLess200() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        assertDoesNotThrow(() -> FilmValidator.validate(film));

        film.setDescription("a".repeat(200));
        assertDoesNotThrow(() -> FilmValidator.validate(film));

        film.setDescription("a".repeat(210));
        assertThrows(FilmValidationException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void releaseDateShouldBeAfter28121895() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        assertDoesNotThrow(() -> FilmValidator.validate(film));

        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> FilmValidator.validate(film));

        film.setReleaseDate(LocalDate.of(1885, 12, 28));
        assertThrows(FilmValidationException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void durationShouldBePositive() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        assertDoesNotThrow(() -> FilmValidator.validate(film));

        film.setDuration(0);
        assertThrows(FilmValidationException.class, () -> FilmValidator.validate(film));

        film.setDuration(-50);
        assertThrows(FilmValidationException.class, () -> FilmValidator.validate(film));
    }
}