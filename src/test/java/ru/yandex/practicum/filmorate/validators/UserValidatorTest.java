package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidatorTest {
    @Test
    void shouldReturnHappyPath() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void emailShouldNotBeEmptyAndContainsAt() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));

        user.setEmail("email");
        assertThrows(UserValidationException.class, () -> UserValidator.validate(user));

        user.setEmail("");
        assertThrows(UserValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void loginShouldBeFilledAndSolidly() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));

        user.setLogin("");
        assertThrows(UserValidationException.class, () -> UserValidator.validate(user));

        user.setLogin("login login");
        assertThrows(UserValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    void birthdateShouldBeBeforeNow() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        assertDoesNotThrow(() -> UserValidator.validate(user));

        user.setBirthday(LocalDate.of(2000, 10, 10));
        assertDoesNotThrow(() -> UserValidator.validate(user));

        user.setBirthday(LocalDate.of(2024, 10, 23));
        assertThrows(UserValidationException.class, () -> UserValidator.validate(user));
    }
}