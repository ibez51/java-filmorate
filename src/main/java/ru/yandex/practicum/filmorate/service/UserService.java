package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    @Qualifier("dbStorage")
    private final UserStorage userStorage;
    private final FriendshipService friendshipService;

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers().stream()
                .map(this::enrichUser)
                .collect(Collectors.toList());
    }

    public User findUser(Integer userId) {
        User user = userStorage.findUser(userId);

        if (Objects.isNull(user)) {
            throw new NullPointerException("Пользователь с Id = " + userId + " не найден.");
        }

        enrichUser(user);

        log.info("User found: id = {} login = {}", user.getId(), user.getLogin());

        return user;
    }

    public User addUser(User user) {
        UserValidator.validate(user);

        if (Objects.isNull(user.getName())
                || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        Integer userId = userStorage.addUser(user);

        return findUser(userId);
    }

    public User updateUser(User user) {
        findUser(user.getId());//check that user exists

        UserValidator.validate(user);

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        userStorage.updateUser(user);

        return findUser(user.getId());
    }

    public User addFriends(Integer userId, Integer friendId) {
        findUser(userId);//check that user exists
        findUser(friendId);//check that user exists

        friendshipService.addFriends(userId, friendId);

        return findUser(userId);
    }

    public User removeFriend(Integer userId, Integer friendId) {
        friendshipService.removeFriend(userId, friendId);

        return findUser(userId);
    }

    public Set<User> getFriendsList(Integer userId) {
        return friendshipService.getFriendsForUser(userId).stream()
                .map(this::findUser)
                .collect(Collectors.toSet());
    }

    public Set<User> getMutualFriends(Integer userId, Integer otherUserId) {
        return friendshipService.getMutualFriends(userId, otherUserId).stream()
                .map(this::findUser)
                .collect(Collectors.toSet());
    }

    private User enrichUser(User user) {
        user.setFriendsSet(friendshipService.getFriendsForUser(user.getId()));

        return user;
    }
}
