package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

public class UserValidator {

    public static void validate(User user) {
        if (Objects.isNull(user.getEmail())
                || user.getEmail().isBlank()
                || !user.getEmail().contains("@")) {
            throw new UserValidationException("Электронная почта пользователя не может быть пустой и должна содержать символ @");
        }

        if (Objects.isNull(user.getLogin())
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            throw new UserValidationException("Логин пользователя не может быть пустым или содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidationException("Дата рождения пользователя не может быть в будущем");
        }
    }
}
