package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    Collection<User> getAllUsers();

    User findUser(Integer userId);

    User addUser(User user);

    User updateUser(User user);

    User addFriends(Integer userId, Integer friendId);

    User removeFriend(Integer userId, Integer friendId);

    Set<User> getFriendsList(Integer userId);

    Set<User> getMutualFriends(Integer userId, Integer otherUserId);

    void resetIdNumberSeq();
}
