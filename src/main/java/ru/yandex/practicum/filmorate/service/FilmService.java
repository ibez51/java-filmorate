package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmService {
    @Qualifier("dbStorage")
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final UserService userService;
    private final FilmLikesService filmLikesService;
    private final MpaRatingService mpaRatingService;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms().stream()
                .map(this::enrichFilm)
                .sorted(Comparator.comparingInt(Film::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Film getFilm(Integer filmId) {
        Film film = filmStorage.findFilm(filmId);

        if (Objects.isNull(film)) {
            throw new NullPointerException("Фильм с Id = " + filmId + " не найден.");
        }

        enrichFilm(film);

        log.info("Film found: id = {} name = {}", film.getId(), film.getName());

        return film;
    }

    public Film addFilm(Film film) {
        FilmValidator.validate(film);

        Integer filmId = filmStorage.addFilm(film);

        if (!Objects.isNull(film.getGenres())
                && !film.getGenres().isEmpty()) {
            for (Integer genreId : film.getGenres().stream().map(Genre::getId).collect(Collectors.toList())) {
                genreService.addFilmGenre(filmId, genreId);
            }
        }

        log.info("Created film with id {}", filmId);

        return getFilm(filmId);
    }

    public Film updateFilm(Film film) {
        FilmValidator.validate(film);

        getFilm(film.getId());//check that film exists

        filmStorage.updateFilm(film);

        genreService.deleteAllFilmGenres(film.getId());

        if (!Objects.isNull(film.getGenres())
                && !film.getGenres().isEmpty()) {
            for (Integer genreId : film.getGenres().stream().map(Genre::getId).collect(Collectors.toList())) {
                genreService.addFilmGenre(film.getId(), genreId);
            }
        }

        log.info("Film with id = {} was updated", film.getId());

        return getFilm(film.getId());
    }

    public Film likeFilm(Integer filmId, Integer userId) {
        getFilm(filmId);//check that film exists
        userService.findUser(userId);//check that user exists

        filmLikesService.likeFilm(filmId, userId);

        log.info("Film with id {} liked", filmId);

        return getFilm(filmId);
    }

    public Film dislikeFilm(Integer filmId, Integer userId) {
        getFilm(filmId);//check that film exists
        userService.findUser(userId);//check that user exists

        filmLikesService.dislikeFilm(filmId, userId);

        log.info("Film with id {} disliked", filmId);

        return getFilm(filmId);
    }

    public Set<Film> getTopPopularFilms(Integer filmsCount) {
        return filmLikesService.getTopPopularFilms(filmsCount).stream()
                .map(x -> getFilm(x.getFilmId()))
                .collect(Collectors.toSet());
    }

    private Film enrichFilm(Film film) {
        film.setMpa(mpaRatingService.getRatingById(film.getMpa().getId()));
        film.setGenres(genreService.findGenresByFilmId(film.getId()));
        film.setUserLikesSet(filmLikesService.findLikesListByFilm(film.getId()).stream()
                .map(FilmLikes::getUserId).collect(Collectors.toSet()));

        return film;
    }
}
