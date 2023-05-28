package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    @Qualifier("dbStorage")
    private final FriendshipDao friendshipDao;

    public Set<Integer> getFriendsForUser(Integer userId) {
        return friendshipDao.getFriendsForUser(userId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());
    }

    public void addFriends(Integer userId, Integer friendId) {
        if (!friendshipDao.isFriendshipExists(userId, friendId)) {
            friendshipDao.addFriends(userId, friendId);
        }

        if (friendshipDao.isFriendshipExists(friendId, userId)) {
            friendshipDao.updateFriendshipStatus(userId, friendId, 2);
        }
    }

    public Set<Integer> getMutualFriends(Integer userId, Integer otherUserId) {
        return friendshipDao.getMutualFriends(userId, otherUserId).stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        friendshipDao.removeFriend(userId, friendId);

        friendshipDao.updateFriendshipStatus(userId, friendId, 1);
    }
}
