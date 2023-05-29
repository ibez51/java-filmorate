package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Component
@Qualifier("inMemoryStorage")
public class InMemoryGenreStorage implements GenreDao {
    @Override
    public List<Genre> findGenresByFilmId(Integer filmId) {
        return List.of(Genre.builder().build());
    }

    @Override
    public List<Genre> findGenreById(Integer id) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public boolean isFilmGenreExists(Integer filmId, Integer genreId) {
        return false;
    }

    @Override
    public void addFilmGenre(Integer filmId, Integer genreId) {

    }

    @Override
    public void deleteAllFilmGenres(Integer filmId) {

    }
}
