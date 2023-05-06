package ru.yandex.practicum.filmorate.FilmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static int idNumberSeq = 0;
    private final Map<Integer, Film> filmsMap = new HashMap<>();

    public Collection<Film> getAllFilms() {
        return filmsMap.values();
    }

    public Film findFilm(Integer filmId) {
        Film film = findFilmById(filmId);

        if (Objects.isNull(film)) {
            throw new NullPointerException("Фильм с Id = " + filmId + " не найден.");
        }

        return film;
    }

    public Film addFilm(Film film) {
        FilmValidator.validate(film);

        film.setId(getNextId());
        filmsMap.put(film.getId(), film);

        log.info("Film {} added successfully", film);

        return film;
    }

    public Film updateFilm(Film film) {
        FilmValidator.validate(film);

        if (filmsMap.containsKey(film.getId())) {
            filmsMap.put(film.getId(), film);

            log.info("Film {} added successfully", film);
        } else {
            log.warn("Film with id {} wasn't found", film.getId());

            throw new NullPointerException("Фильм с Id = " + film.getId() + " не найден");
        }

        return film;
    }

    private static int getNextId() {
        idNumberSeq++;

        return idNumberSeq;
    }

    public void resetIdNumberSeq() {
        idNumberSeq = 0;
    }

    public Film findFilmById(Integer filmId) {
        return filmsMap.get(filmId);
    }
}
