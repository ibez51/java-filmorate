package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipDao {
    List<Friendship> getFriendsForUser(Integer userId);

    void addFriends(Integer userId, Integer friendId);

    boolean isFriendshipExists(Integer userId, Integer friendId);

    void updateFriendshipStatus(Integer userId, Integer friendId, Integer friendshipStatusId);

    List<Friendship> getMutualFriends(Integer userId, Integer otherUserId);

    void removeFriend(Integer userId, Integer friendId);
}
