package ru.yandex.practicum.filmorate.FilmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film findFilmById(Integer filmId);

    Collection<Film> getAllFilms();

    Film findFilm(Integer filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void resetIdNumberSeq();
}
