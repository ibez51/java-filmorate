package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenreService {
    @Qualifier("dbStorage")
    private final GenreDao genreDao;

    public List<Genre> findGenresByFilmId(Integer filmId) {
        return genreDao.findGenresByFilmId(filmId);
    }

    public Genre findGenreById(Integer id) {
        Genre genre = genreDao.findGenreById(id).stream().findFirst().orElseThrow(() -> new NullPointerException("Жанр с Id " + id + " не найден."));

        log.info("Genre found: id = {} name = {}", genre.getId(), genre.getName());

        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public void addFilmGenre(Integer filmId, Integer genreId) {
        if (!genreDao.isFilmGenreExists(filmId, genreId)) {
            genreDao.addFilmGenre(filmId, genreId);
        }
    }

    public void deleteAllFilmGenres(Integer filmId) {
        genreDao.deleteAllFilmGenres(filmId);
    }
}
