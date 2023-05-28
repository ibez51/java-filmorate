package ru.yandex.practicum.filmorate.storage.Dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Qualifier("dbStorage")
@AllArgsConstructor
public class FriendshipDaoImpl implements FriendshipDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Friendship> getFriendsForUser(Integer userId) {
        String sql = "SELECT user_id, friend_id, friendship_status_id FROM friends_link " +
                "WHERE user_id = ?;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriendship(rs), userId);
    }

    @Override
    public boolean isFriendshipExists(Integer userId, Integer friendId) {
        String sql = "SELECT 'x' FROM friends_link " +
                "WHERE user_id = ? " +
                "AND friend_id = ? " +
                "LIMIT 1;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, friendId);

        return rowSet.next();
    }

    @Override
    public void addFriends(Integer userId, Integer friendId) {
        String sql = "INSERT INTO friends_link " +
                "(user_id, friend_id, friendship_status_id) " +
                "VALUES (?, ?, 1);";

        jdbcTemplate.update(sql, userId, friendId);

        log.info("User with id = {} was added to friends with {}", userId, friendId);
    }

    @Override
    public void updateFriendshipStatus(Integer userId, Integer friendId, Integer friendshipStatusId) {
        String sql = "UPDATE friends_link " +
                "SET friendship_status_id = ? " +
                "WHERE (user_id = ? " +
                "AND friend_id = ?) " +
                "OR (user_id = ? " +
                "AND friend_id = ?);";
        jdbcTemplate.update(sql, friendshipStatusId, userId, friendId, friendId, userId);

        log.info("Friendship status was updated for users {} and {}", userId, friendId);
    }

    @Override
    public List<Friendship> getMutualFriends(Integer userId, Integer otherUserId) {
        String sql = "SELECT user_id, friend_id, friendship_status_id FROM friends_link AS userFriends " +
                "WHERE userFriends.user_id = ? " +
                "AND EXISTS (SELECT 'x' FROM friends_link AS otherUserFriends " +
                "WHERE otherUserFriends.user_id = ? " +
                "AND otherUserFriends.friend_id = userFriends.friend_id);";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriendship(rs), userId, otherUserId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM friends_link " +
                "WHERE user_id = ? " +
                "AND friend_id = ?;";

        jdbcTemplate.update(sql, userId, friendId);
    }

    private Friendship makeFriendship(ResultSet rs) throws SQLException {
        return Friendship.builder()
                .userId(rs.getInt("user_id"))
                .friendId(rs.getInt("friend_id"))
                .friendshipStatus(rs.getInt("friendship_status_id"))
                .build();
    }
}
