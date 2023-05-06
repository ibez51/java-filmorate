package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.FilmStorage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriends(Integer userId, Integer friendId) {
        User user = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);

        user.getFriendsSet().add(friendId);
        friend.getFriendsSet().add(userId);

        return user;
    }

    public User removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUser(userId);
        User friend = userStorage.findUser(friendId);

        user.getFriendsSet().remove(friendId);
        friend.getFriendsSet().remove(userId);

        return user;
    }

    public Set<User> getFriendsList(Integer userId) {
        User user = userStorage.findUser(userId);

        return user.getFriendsSet().stream()
                .sorted(Integer::compareTo)
                .map(userStorage::findUserById)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<User> getMutualFriends(Integer userId, Integer otherUserId) {
        User user = userStorage.findUser(userId);
        User otherUser = userStorage.findUser(otherUserId);

        return user.getFriendsSet().stream()
                .filter(x -> otherUser.getFriendsSet().contains(x))
                .map(userStorage::findUserById)
                .collect(Collectors.toSet());
    }
}
