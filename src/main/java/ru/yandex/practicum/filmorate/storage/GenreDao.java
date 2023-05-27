package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> findGenresByFilmId(Integer filmId);

    Genre findGenreById(Integer id);

    List<Genre> getAllGenres();

    void addFilmGenre(Integer filmId, Integer genreId);

    void deleteAllFilmGenres(Integer filmId);
}
