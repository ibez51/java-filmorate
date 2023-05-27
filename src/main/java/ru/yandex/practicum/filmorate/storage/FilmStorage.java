package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film findFilm(Integer filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film likeFilm(Integer filmId, Integer userId);

    Film dislikeFilm(Integer filmId, Integer userId);

    Set<Film> getTopPopularFilms(Integer filmsCount);

    void resetIdNumberSeq();
}
