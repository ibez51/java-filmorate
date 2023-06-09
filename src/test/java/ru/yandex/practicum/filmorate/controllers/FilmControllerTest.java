package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.inMemory.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;
    FilmStorage filmStorage;
    FilmService filmService;
    FriendshipService friendshipService;
    UserStorage userStorage;
    UserService userService;
    MpaRatingService mpaRatingService;
    GenreService genreService;
    FilmLikesService filmLikesService;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage(userStorage);

        friendshipService = new FriendshipService(new InMemoryFriendshipStorage(userStorage));
        userService = new UserService(userStorage, friendshipService);
        mpaRatingService = new MpaRatingService(new InMemoryMpaRatingStorage());
        genreService = new GenreService(new InMemoryGenreStorage());
        filmLikesService = new FilmLikesService(new InMemoryFilmLikesStorage(filmStorage));
        filmService = new FilmService(filmStorage, genreService, userService, filmLikesService, mpaRatingService);

        filmController = new FilmController(filmService);

        filmStorage.resetIdNumberSeq();
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
        assertThrows(NullPointerException.class, () -> filmController.updateFilm(film2));
    }

    @Test
    void testGetAllFilms() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film);

        Film film2 = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film2);

        assertEquals(2, filmController.getAllFilms().size());
    }

    @Test
    void testGetFilm() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film);

        Film film2 = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film2);

        assertEquals(film, filmController.getFilm(film.getId()));

        assertThrows(NullPointerException.class, () -> filmController.getFilm(999));
    }

    @Test
    void testLikeFilm() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film);

        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userStorage.addUser(user);

        assertEquals(0, film.getUserLikesSet().size());

        filmController.likeFilm(film.getId(), user.getId());

        assertEquals(1, film.getUserLikesSet().size());
    }

    @Test
    void testDislikeFilm() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film);

        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userStorage.addUser(user);

        User user2 = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userStorage.addUser(user2);

        filmController.likeFilm(film.getId(), user.getId());

        assertEquals(1, film.getUserLikesSet().size());

        filmController.dislikeFilm(film.getId(), user.getId());

        assertEquals(0, film.getUserLikesSet().size());

        assertThrows(NullPointerException.class, () -> filmController.dislikeFilm(film.getId(), user2.getId()));
    }

    @Test
    void testPopularFilm() {
        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        filmController.addFilm(film);

        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userStorage.addUser(user);

        filmController.likeFilm(film.getId(), user.getId());

        assertEquals(1, filmController.getPopularFilms(5).size());
    }
}