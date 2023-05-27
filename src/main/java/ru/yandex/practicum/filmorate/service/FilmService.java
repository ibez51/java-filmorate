package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("dbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(Integer filmId) {
        return filmStorage.findFilm(filmId);
    }

    public Film addFilm(Film film) {
        FilmValidator.validate(film);

        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        FilmValidator.validate(film);

        return filmStorage.updateFilm(film);
    }

    public Film likeFilm(Integer filmId, Integer userId) {
        return filmStorage.likeFilm(filmId, userId);
    }

    public Film dislikeFilm(Integer filmId, Integer userId) {
        return filmStorage.dislikeFilm(filmId, userId);
    }

    public Set<Film> getTopPopularFilms(Integer filmsCount) {
        return filmStorage.getTopPopularFilms(filmsCount);
    }
}
