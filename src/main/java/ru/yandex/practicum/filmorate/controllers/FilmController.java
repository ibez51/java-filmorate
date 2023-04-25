package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static int idNumberSeq = 0;
    Map<Integer, Film> filmsMap = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmsMap.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        String filmValidationResult = FilmValidator.validate(film);

        if (!filmValidationResult.isBlank()) {
            log.warn("Film {} has validation errors: {}", film, filmValidationResult);

            throw new FilmValidationException(filmValidationResult);
        }

        film.setId(getNextId());
        filmsMap.put(film.getId(), film);

        log.info("Film {} added successfully", film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        String filmValidationResult = FilmValidator.validate(film);

        if (!filmValidationResult.isBlank()) {
            log.warn("Film {} has validation errors: {}", film, filmValidationResult);

            throw new FilmValidationException(filmValidationResult);
        }

        if (filmsMap.containsKey(film.getId())) {
            filmsMap.put(film.getId(), film);

            log.info("Film {} added successfully", film);
        } else {
            log.warn("Film with id {} wasn't found", film.getId());

            throw new FilmValidationException("Фильм с Id = " + film.getId() + " не найден");
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
}
