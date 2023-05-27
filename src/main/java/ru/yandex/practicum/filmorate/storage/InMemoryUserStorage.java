package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("inMemoryStorage")
public class InMemoryUserStorage implements UserStorage {
    private static int idNumberSeq = 0;
    private final Map<Integer, User> usersMap = new HashMap<>();

    public Collection<User> getAllUsers() {
        return usersMap.values();
    }

    public User findUser(Integer userId) {
        User user = usersMap.get(userId);

        if (Objects.isNull(user)) {
            throw new NullPointerException("Пользователь с Id = " + userId + " не найден.");
        }

        return user;
    }

    public User addUser(User user) {
        user.setId(getNextId());
        usersMap.put(user.getId(), user);

        log.info("User {} added successfully", user);

        return user;
    }

    public User updateUser(User user) {
        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);

            log.info("User {} added successfully", user);
        } else {
            log.warn("User with id {} wasn't found", user.getId());

            throw new NullPointerException("пользователь с Id = " + user.getId() + " не найден");
        }

        return user;
    }

    @Override
    public User addFriends(Integer userId, Integer friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);

        user.getFriendsSet().add(friendId);
        friend.getFriendsSet().add(userId);

        return user;
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);

        user.getFriendsSet().remove(friendId);
        friend.getFriendsSet().remove(userId);

        return user;
    }

    @Override
    public Set<User> getFriendsList(Integer userId) {
        User user = findUser(userId);

        return user.getFriendsSet().stream()
                .sorted(Integer::compareTo)
                .map(this::findUser)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<User> getMutualFriends(Integer userId, Integer otherUserId) {
        User user = findUser(userId);
        User otherUser = findUser(otherUserId);

        return user.getFriendsSet().stream()
                .filter(x -> otherUser.getFriendsSet().contains(x))
                .map(this::findUser)
                .collect(Collectors.toSet());
    }

    private static int getNextId() {
        idNumberSeq++;

        return idNumberSeq;
    }

    @Override
    public void resetIdNumberSeq() {
        idNumberSeq = 0;
    }
}
