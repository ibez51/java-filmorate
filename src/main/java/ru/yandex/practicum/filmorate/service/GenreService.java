package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> findGenresByFilmId(Integer filmId) {
        return genreDao.findGenresByFilmId(filmId);
    }

    public Genre findGenreById(Integer id) {
        return genreDao.findGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public void addFilmGenre(Integer filmId, Integer genreId) {
        genreDao.addFilmGenre(filmId, genreId);
    }

    public void deleteAllFilmGenres(Integer filmId) {
        genreDao.deleteAllFilmGenres(filmId);
    }
}
