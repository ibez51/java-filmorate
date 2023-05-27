package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    UserController userController;
    UserStorage userStorage;
    UserService userService;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        //userController = new UserController(userStorage, userService);
        userController = new UserController(userService);

        userStorage.resetIdNumberSeq();
    }

    @Test
    void testAddUser() {
        User user = User.builder().email("Email@").login("Login").birthday(LocalDate.now()).build();
        User retUser = userController.addUser(user);

        assertEquals(retUser, user);

        user.setLogin("");
        assertThrows(UserValidationException.class, () -> userController.addUser(user));
    }

    @Test
    void testUpdateUser() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User user1 = User.builder().email("Email@1").login("Login1").name("").birthday(LocalDate.now()).build();
        user1.setId(1);

        User retUser = userController.updateUser(user1);

        assertEquals(retUser, user1);

        user.setEmail("");
        assertThrows(UserValidationException.class, () -> userController.updateUser(user));

        User user2 = User.builder().id(50).email("Email@1").login("Login1").name("Name1").birthday(LocalDate.now()).build();
        assertThrows(NullPointerException.class, () -> userController.updateUser(user2));
    }

    @Test
    void testGetAllUsers() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User user2 = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user2);

        assertEquals(2, userController.getAllUsers().size());
    }

    @Test
    void testGetUser() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User user2 = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user2);

        assertEquals(user, userController.getUser(user.getId()));

        assertThrows(NullPointerException.class, () -> userController.getUser(999));
    }

    @Test
    void testAddFriend() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User friend = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(friend);

        user = userController.addFriend(1, 2);

        assertEquals(1, user.getFriendsSet().size());
    }

    @Test
    void testRemoveFriend() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User friend = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(friend);

        user = userController.addFriend(1, 2);

        assertEquals(1, user.getFriendsSet().size());

        user = userController.removeFriend(user.getId(), friend.getId());

        assertEquals(0, user.getFriendsSet().size());
    }

    @Test
    void testFriendsList() {
        User user = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user);

        User friend = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(friend);

        user = userController.addFriend(1, 2);

        assertEquals(1, userController.friendsList(user.getId()).size());
    }


    @Test
    void testMutualFriendsList() {
        User user1 = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user1);

        User user2 = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(user2);

        User commonFriend = User.builder().email("Email@").login("Login").name("Name").birthday(LocalDate.now()).build();
        userController.addUser(commonFriend);

        userController.addFriend(user1.getId(), commonFriend.getId());
        userController.addFriend(user2.getId(), commonFriend.getId());

        assertEquals(1, userController.mutualFriends(user1.getId(), user2.getId()).size());
    }
}