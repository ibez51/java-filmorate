package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.storage.FilmLikesDao;

import java.util.List;

@Service
public class FilmLikesService {
    private final FilmLikesDao filmLikesDao;

    public FilmLikesService(FilmLikesDao filmLikesDao) {
        this.filmLikesDao = filmLikesDao;
    }

    public List<FilmLikes> getTopPopularFilms(Integer filmsCount) {
        return filmLikesDao.getTopPopularFilms(filmsCount);
    }

    public void likeFilm(Integer filmId, Integer userId) {
        filmLikesDao.likeFilm(filmId, userId);
    }

    public void dislikeFilm(Integer filmId, Integer userId) {
        filmLikesDao.dislikeFilm(filmId, userId);
    }

    public List<FilmLikes> findLikesListByFilm(Integer filmId) {
        return filmLikesDao.findLikesListByFilm(filmId);
    }
}
