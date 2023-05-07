package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Film findFilm(Integer filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void resetIdNumberSeq();
}
