package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmStorage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    public final UserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage inMemoryUserStorage,
                          UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(name = "id") Integer userId) {
        return inMemoryUserStorage.findUser(userId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(name = "id") Integer userId,
                          @PathVariable(name = "friendId") Integer friendId) {
        return userService.addFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable(name = "id") Integer userId,
                             @PathVariable(name = "friendId") Integer friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> friendsList(@PathVariable(name = "id") Integer userId) {
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> mutualFriends(@PathVariable(name = "id") Integer userId,
                                   @PathVariable(name = "otherId") Integer otherUserId) {
        return userService.getMutualFriends(userId, otherUserId);
    }
}
