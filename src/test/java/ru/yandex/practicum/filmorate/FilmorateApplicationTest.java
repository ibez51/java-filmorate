package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaRatingService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTest {
    private final UserService userService;
    private final FilmService filmService;
    private final MpaRatingService mpaRatingService;
    private final GenreService genreService;
    private static Integer userId1 = 0;
    private static Integer userId2 = 0;
    private static Integer userId3 = 0;
    private static Integer filmId1 = 0;
    private static Integer filmId2 = 0;
    private static Integer filmId3 = 0;

    @BeforeEach
    public void beforeEach() {
        User user = User.builder().email("Email@").login("Login").birthday(LocalDate.now()).build();
        if (userId1 == 0) {
            userId1 = userService.addUser(user).getId();
        }
        if (userId2 == 0) {
            userId2 = userService.addUser(user).getId();
        }
        if (userId3 == 0) {
            userId3 = userService.addUser(user).getId();
        }

        userService.addFriends(userId1, userId2);
        userService.addFriends(userId1, userId3);
        userService.addFriends(userId2, userId3);

        Film film = Film.builder().name("Film name").description("Film description").releaseDate(LocalDate.now()).duration(50).build();
        if (filmId1 == 0) {
            filmId1 = filmService.addFilm(film).getId();
        }
        if (filmId2 == 0) {
            filmId2 = filmService.addFilm(film).getId();
        }
        if (filmId3 == 0) {
            filmId3 = filmService.addFilm(film).getId();
        }

        filmService.likeFilm(filmId1, userId1);
    }

    @Test
    public void testGetAllUsers() {
        assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    public void testFindUser() {
        assertEquals(1, userService.findUser(userId1).getId());

        assertThrows(NullPointerException.class, () -> userService.findUser(999));
    }

    @Test
    public void testAddUser() {
        assertEquals(2, userService.findUser(userId2).getId());
    }

    @Test
    public void testUpdateUser() {
        User user = userService.findUser(userId1);
        user.setLogin("Login1");

        user = userService.updateUser(user);

        assertEquals("Login1", user.getLogin());
    }

    @Test
    public void testAddFriends() {
        assertEquals(2, userService.findUser(userId1).getFriendsSet().size());
    }

    @Test
    public void testRemoveFriend() {
        User user = userService.removeFriend(userId1, userId2);

        assertEquals(1, user.getFriendsSet().size());

        userService.addFriends(userId1, userId2);
    }

    @Test
    public void testGetFriendsList() {
        assertEquals(2, userService.getFriendsList(userId1).size());
    }

    @Test
    public void testGetMutualFriends() {
        assertEquals(1, userService.getMutualFriends(userId1, userId2).size());
    }

    @Test
    public void testGetAllFilms() {
        assertEquals(3, filmService.getAllFilms().size());
    }

    @Test
    public void testGetFilm() {
        assertEquals(1, filmService.getFilm(filmId1).getId());

        assertThrows(NullPointerException.class, () -> filmService.getFilm(999));
    }

    @Test
    public void testAddFilm() {
        assertEquals(2, filmService.getFilm(filmId2).getId());
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmService.getFilm(1);
        film.setName("Film New Name");

        assertEquals("Film New Name", filmService.updateFilm(film).getName());
    }

    @Test
    public void testLikeFilm() {
        assertEquals(1, filmService.getFilm(filmId1).getUserLikesSet().size());
    }

    @Test
    public void testDislikeFilm() {
        assertEquals(0, filmService.dislikeFilm(filmId1, userId1).getUserLikesSet().size());

        filmService.likeFilm(filmId1, userId1);
    }

    @Test
    public void testGetTopPopularFilms() {
        assertEquals(1, filmService.getTopPopularFilms(1).size());
    }

    @Test
    public void testGetAllRatings() {
        assertEquals(5, mpaRatingService.getAllRatings().size());
    }

    @Test
    public void testFindGenreById() {
        assertEquals("Комедия", genreService.findGenreById(1).getName());
    }

    @Test
    public void testGetAllGenres() {
        assertEquals(6, genreService.getAllGenres().size());
    }

    @Test
    public void testAddFilmGenre() {
        genreService.addFilmGenre(filmId1, 2);
        assertEquals(2, filmService.getFilm(filmId1).getGenres().stream().findFirst().get().getId());
    }
}