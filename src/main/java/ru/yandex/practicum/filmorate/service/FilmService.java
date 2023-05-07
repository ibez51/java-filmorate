package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film likeFilm(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilm(filmId);
        userStorage.findUser(userId);//check is User exists

        film.getUserLikesSet().add(userId);

        return film;
    }

    public Film dislikeFilm(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilm(filmId);
        User user = userStorage.findUser(userId);

        if (!film.getUserLikesSet().contains(userId)) {
            throw new NullPointerException("Не найден лайк пользователя " + userId);
        }

        film.getUserLikesSet().remove(user.getId());

        return film;
    }

    public Set<Film> getTopPopularFilms(Integer filmsCount) {
        return filmStorage.getAllFilms().stream()
                .sorted((x, y) -> y.getUserLikesSet().size() - x.getUserLikesSet().size())
                .limit(filmsCount)
                .collect(Collectors.toSet());
    }
}
