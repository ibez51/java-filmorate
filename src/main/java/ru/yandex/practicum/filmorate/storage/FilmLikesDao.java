package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.List;

public interface FilmLikesDao {
    List<FilmLikes> getTopPopularFilms(Integer filmsCount);

    void likeFilm(Integer filmId, Integer userId);

    void dislikeFilm(Integer filmId, Integer userId);

    List<FilmLikes> findLikesListByFilm(Integer filmId);
}
