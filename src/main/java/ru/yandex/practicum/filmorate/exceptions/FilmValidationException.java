package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException(final String message) {
        super(message);
    }
}
