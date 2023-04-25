package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void init() {
        filmController = new FilmController();

        filmController.resetIdNumberSeq();
    }

    @Test
    void testAddFilm() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        Film retFilm = filmController.addFilm(film);

        assertEquals(retFilm, film);

        film.setName("");
        assertThrows(FilmValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testUpdateFilm() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film);

        Film film1 = Film.builder().name("Film name1").description("Film description1").releaseDate(LocalDate.now()).duration(50).build();
        film1.setId(1);

        Film retFilm = filmController.updateFilm(film1);

        assertEquals(retFilm, film1);

        film.setName("");
        assertThrows(FilmValidationException.class, () -> filmController.updateFilm(film));

        Film film2 = Film.builder().id(50).name("Film name1").description("Film description1").releaseDate(LocalDate.now()).duration(50).build();
        assertThrows(FilmValidationException.class, () -> filmController.updateFilm(film2));
    }

    @Test
    void testGetAllFilms() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film);

        Film film2 = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film2);

        assertEquals(2, filmController.getAllFilms().size());
    }
}