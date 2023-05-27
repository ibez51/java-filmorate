package ru.yandex.practicum.filmorate.storage.Dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("dbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipService friendshipService;

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT id, email, login, name, birthdate FROM user_table";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findUser(Integer userId) {
        User user;
        String sql = "SELECT id, email, login, name, birthdate FROM user_table " +
                "WHERE id = ? " +
                "LIMIT 1;";
        List<User> resultList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);

        if (resultList.size() == 1) {
            user = resultList.get(0);

            log.info("User found: id = {} login = {}", user.getId(), user.getLogin());

            return user;
        } else {
            log.info("user with id = {} not found", userId);

            throw new NullPointerException("Пользователь с Id = " + userId + " не найден.");
        }
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert insertIntoUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("user_table").usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("birthdate", user.getBirthday());

        Integer userId = insertIntoUser.executeAndReturnKey(parameters).intValue();

        log.info("Created user with id {}", userId);

        return findUser(userId);
    }

    @Override
    public User updateUser(User user) {
        findUser(user.getId());//check that user exists

        int rowsAffected = jdbcTemplate.update("UPDATE user_table " +
                        "SET email = ?, " +
                        "login = ?, " +
                        "name = ?, " +
                        "birthdate = ? " +
                        "WHERE id = ?;",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (rowsAffected == 0) {
            throw new RuntimeException("User with id = " + user.getId() + " wasn't updated");
        }

        log.info("User with id = {} was updated", user.getId());

        return findUser(user.getId());
    }

    @Override
    public User addFriends(Integer userId, Integer friendId) {
        findUser(userId);//check that user exists
        findUser(friendId);//check that user exists

        friendshipService.addFriends(userId, friendId);

        return findUser(userId);
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        friendshipService.removeFriend(userId, friendId);

        return findUser(userId);
    }

    @Override
    public Set<User> getFriendsList(Integer userId) {
        return friendshipService.getFriendsForUser(userId).stream()
                .map(this::findUser)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getMutualFriends(Integer userId, Integer otherUserId) {
        return friendshipService.getMutualFriends(userId, otherUserId).stream()
                .map(this::findUser)
                .collect(Collectors.toSet());
    }

    @Override
    public void resetIdNumberSeq() {
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthdate").toLocalDate())
                .friendsSet(friendshipService.getFriendsForUser(rs.getInt("id")))
                .build();
    }
}
