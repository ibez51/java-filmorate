package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    public static String validate(User user) {
        StringBuilder retBuilder = new StringBuilder();

        if (user.getEmail().isBlank()
                || !user.getEmail().contains("@")) {
            retBuilder.append("Электронная почта пользователя не может быть пустой и должна содержать символ @").append(System.lineSeparator());
        }

        if (user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            retBuilder.append("Логин пользователя не может быть пустым или содержать пробелы").append(System.lineSeparator());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            retBuilder.append("Дата рождения пользователя не может быть в будущем").append(System.lineSeparator());
        }

        return retBuilder.toString();
    }
}
