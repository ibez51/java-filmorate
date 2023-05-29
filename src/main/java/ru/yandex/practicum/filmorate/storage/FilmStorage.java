package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film findFilm(Integer filmId);

    Integer addFilm(Film film);

    void updateFilm(Film film);

    Film likeFilm(Integer filmId, Integer userId);

    Film dislikeFilm(Integer filmId, Integer userId);

    Set<Film> getTopPopularFilms(Integer filmsCount);

    void resetIdNumberSeq();
}
