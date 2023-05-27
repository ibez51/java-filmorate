package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    private final FriendshipDao friendshipDao;

    public FriendshipService(FriendshipDao friendshipDao) {
        this.friendshipDao = friendshipDao;
    }

    public Set<Integer> getFriendsForUser(Integer userId) {
        return friendshipDao.getFriendsForUser(userId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());
    }

    public void addFriends(Integer userId, Integer friendId) {
        friendshipDao.addFriends(userId, friendId);
    }

    public Set<Integer> getMutualFriends(Integer userId, Integer otherUserId) {
        return friendshipDao.getMutualFriends(userId, otherUserId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        friendshipDao.removeFriend(userId, friendId);
    }
}
