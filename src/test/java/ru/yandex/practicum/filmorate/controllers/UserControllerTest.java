package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    UserController userController;

    @BeforeEach
    void init() {
        userController = new UserController();

        userController.resetIdNumberSeq();
    }

    @Test
    void testAddUser() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        User retUser = userController.addUser(user);

        assertEquals(retUser, user);

        user.setLogin("");
        assertThrows(UserValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void testUpdateUser() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User user1 = User.builder().email("Email@1").login("Login1").name("Name1").birthday(LocalDate.now()).build();
        user1.setId(1);

        User retUser = userController.updateUser(user1);

        assertEquals(retUser, user1);

        user.setEmail("");
        assertThrows(UserValidationException.class, () -> userController.updateUser(user));

        User user2 = User.builder().id(50).email("Email@1").login("Login1").name("Name1").birthday(LocalDate.now()).build();
        assertThrows(UserValidationException.class, () -> userController.updateUser(user2));
    }

    @Test
    void testGetAllUsers() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User user2 = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user2);

        assertEquals(2, userController.getAllUsers().size());
    }
}