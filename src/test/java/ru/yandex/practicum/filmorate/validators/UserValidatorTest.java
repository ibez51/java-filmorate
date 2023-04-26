package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserValidatorTest {
    @Test
    void shouldReturnHappyPath() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();

        assertEquals("", UserValidator.validate(user));
    }

    @Test
    void emailShouldNotBeEmptyAndContainsAt() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        assertEquals("", UserValidator.validate(user));

        user.setEmail("email");
        String estimatedValue = "Электронная почта пользователя не может быть пустой и должна содержать символ @" + System.lineSeparator();
        assertEquals(estimatedValue, UserValidator.validate(user));

        user.setEmail("");
        assertEquals(estimatedValue, UserValidator.validate(user));
    }

    @Test
    void loginShouldBeFilledAndSolidly() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        assertEquals("", UserValidator.validate(user));

        user.setLogin("");
        String estimatedValue = "Логин пользователя не может быть пустым или содержать пробелы" + System.lineSeparator();
        assertEquals(estimatedValue, UserValidator.validate(user));

        user.setLogin("login login");
        assertEquals(estimatedValue, UserValidator.validate(user));
    }

    @Test
    void birthdateShouldBeBeforeNow() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        assertEquals("", UserValidator.validate(user));

        user.setBirthday(LocalDate.of(2000, 10, 10));
        assertEquals("", UserValidator.validate(user));

        user.setBirthday(LocalDate.of(2024, 10, 23));
        String estimatedValue = "Дата рождения пользователя не может быть в будущем" + System.lineSeparator();
        assertEquals(estimatedValue, UserValidator.validate(user));
    }
/*
дата рождения не может быть в будущем.
 */
}