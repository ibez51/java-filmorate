package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("dbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User findUser(Integer userId) {
        return userStorage.findUser(userId);
    }

    public User addUser(User user) {
        UserValidator.validate(user);

        if (Objects.isNull(user.getName())
                || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        UserValidator.validate(user);

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return userStorage.updateUser(user);
    }

    public User addFriends(Integer userId, Integer friendId) {
        return userStorage.addFriends(userId, friendId);
    }

    public User removeFriend(Integer userId, Integer friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    public Set<User> getFriendsList(Integer userId) {
        return userStorage.getFriendsList(userId);
    }

    public Set<User> getMutualFriends(Integer userId, Integer otherUserId) {
        return userStorage.getMutualFriends(userId, otherUserId);
    }
}
