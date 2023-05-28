package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.storage.FilmLikesDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmLikesService {
    @Qualifier("dbStorage")
    private final FilmLikesDao filmLikesDao;

    public List<FilmLikes> getTopPopularFilms(Integer filmsCount) {
        return filmLikesDao.getTopPopularFilms(filmsCount);
    }

    public void likeFilm(Integer filmId, Integer userId) {
        if (!filmLikesDao.isLikeExists(filmId, userId)) {
            filmLikesDao.likeFilm(filmId, userId);
        }
    }

    public void dislikeFilm(Integer filmId, Integer userId) {
        filmLikesDao.dislikeFilm(filmId, userId);
    }

    public List<FilmLikes> findLikesListByFilm(Integer filmId) {
        return filmLikesDao.findLikesListByFilm(filmId);
    }
}
