package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
@Qualifier("inMemoryStorage")
public class InMemoryFriendshipStorage implements FriendshipDao {
    private final UserStorage inMemoryUserStorage;

    @Override
    public List<Friendship> getFriendsForUser(Integer userId) {
        return inMemoryUserStorage.getFriendsList(userId).stream()
                .map(x -> Friendship.builder()
                        .userId(userId)
                        .friendId(x.getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void addFriends(Integer userId, Integer friendId) {
        inMemoryUserStorage.addFriends(userId, friendId);
    }

    @Override
    public boolean isFriendshipExists(Integer userId, Integer friendId) {
        return inMemoryUserStorage.getFriendsList(userId).contains(inMemoryUserStorage.findUser(friendId));
    }

    @Override
    public void updateFriendshipStatus(Integer userId, Integer friendId, Integer friendshipStatusId) {
    }

    @Override
    public List<Friendship> getMutualFriends(Integer userId, Integer otherUserId) {
        return inMemoryUserStorage.getMutualFriends(userId, otherUserId).stream()
                .map(x -> Friendship.builder()
                        .userId(userId)
                        .friendId(otherUserId)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        inMemoryUserStorage.removeFriend(userId, friendId);
    }
}
