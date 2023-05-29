package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("inMemoryStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private static int idNumberSeq = 0;
    private final Map<Integer, Film> filmsMap;

    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(@Qualifier("inMemoryStorage") UserStorage userStorage) {
        this.filmsMap = new HashMap<>();
        this.userStorage = userStorage;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmsMap.values();
    }

    @Override
    public Film findFilm(Integer filmId) {
        Film film = filmsMap.get(filmId);

        if (Objects.isNull(film)) {
            throw new NullPointerException("Фильм с Id = " + filmId + " не найден.");
        }

        return film;
    }

    @Override
    public Integer addFilm(Film film) {
        film.setId(getNextId());
        filmsMap.put(film.getId(), film);

        log.info("Film {} added successfully", film);

        return film.getId();
    }

    @Override
    public void updateFilm(Film film) {
        if (filmsMap.containsKey(film.getId())) {
            filmsMap.put(film.getId(), film);

            log.info("Film {} updated successfully", film);
        } else {
            log.warn("Film with id {} wasn't found", film.getId());

            throw new NullPointerException("Фильм с Id = " + film.getId() + " не найден");
        }
    }

    @Override
    public Film likeFilm(Integer filmId, Integer userId) {
        userStorage.findUser(userId);//check is User exists

        Film film = findFilm(filmId);

        film.getUserLikesSet().add(userId);

        return film;
    }

    @Override
    public Film dislikeFilm(Integer filmId, Integer userId) {
        Film film = findFilm(filmId);
        User user = userStorage.findUser(userId);

        if (!film.getUserLikesSet().contains(userId)) {
            throw new NullPointerException("Не найден лайк пользователя " + userId);
        }

        film.getUserLikesSet().remove(user.getId());

        return film;
    }

    @Override
    public Set<Film> getTopPopularFilms(Integer filmsCount) {
        return getAllFilms().stream()
                .sorted((x, y) -> y.getUserLikesSet().size() - x.getUserLikesSet().size())
                .limit(filmsCount)
                .collect(Collectors.toSet());
    }

    private static int getNextId() {
        idNumberSeq++;

        return idNumberSeq;
    }

    @Override
    public void resetIdNumberSeq() {
        idNumberSeq = 0;
    }
}
