package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> findGenresByFilmId(Integer filmId);

    List<Genre> findGenreById(Integer id);

    List<Genre> getAllGenres();

    boolean isFilmGenreExists(Integer filmId, Integer genreId);

    void addFilmGenre(Integer filmId, Integer genreId);

    void deleteAllFilmGenres(Integer filmId);
}
