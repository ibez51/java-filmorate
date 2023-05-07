package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class UserValidator {
    public static void validate(User user) {
        if (Objects.isNull(user.getEmail())) {
            log.warn("User {} has validation errors: field email is null.", user);

            throw new UserValidationException("Электронная почта пользователя обязательна к заполнению.");
        }

        if (user.getEmail().isBlank()) {
            log.warn("User {} has validation errors: field email is blank.", user);

            throw new UserValidationException("Электронная почта пользователя не может быть пустой.");
        }

        if (!user.getEmail().contains("@")) {
            log.warn("User {} has validation errors: field email does not contain @.", user);

            throw new UserValidationException("Электронная почта пользователя должна содержать символ @.");
        }

        if (Objects.isNull(user.getLogin())) {
            log.warn("User {} has validation errors: field login is null.", user);

            throw new UserValidationException("Логин пользователя обязателен к заполнению.");
        }

        if (user.getLogin().isBlank()) {
            log.warn("User {} has validation errors: field login is blank.", user);

            throw new UserValidationException("Логин пользователя не может быть пустым.");
        }

        if (user.getLogin().contains(" ")) {
            log.warn("User {} has validation errors: field login contains space symbol.", user);

            throw new UserValidationException("Логин пользователя не может содержать пробелы.");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("User {} has validation errors: field birthday is after now().", user);

            throw new UserValidationException("Дата рождения пользователя не может быть в будущем.");
        }
    }
}
