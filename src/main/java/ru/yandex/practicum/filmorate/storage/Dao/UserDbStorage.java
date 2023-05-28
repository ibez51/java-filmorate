package ru.yandex.practicum.filmorate.storage.Dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@Qualifier("dbStorage")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT id, email, login, name, birthdate FROM user_table";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findUser(Integer userId) {
        String sql = "SELECT id, email, login, name, birthdate FROM user_table " +
                "WHERE id = ? " +
                "LIMIT 1;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId).stream().findFirst().orElseGet(null);
    }

    @Override
    public Integer addUser(User user) {
        SimpleJdbcInsert insertIntoUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("user_table").usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("birthdate", user.getBirthday());

        Integer userId = insertIntoUser.executeAndReturnKey(parameters).intValue();

        log.info("Created user with id {}", userId);

        return userId;
    }

    @Override
    public void updateUser(User user) {
        jdbcTemplate.update("UPDATE user_table " +
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

        log.info("User with id = {} was updated", user.getId());
    }

    @Override
    public User addFriends(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public User removeFriend(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public Set<User> getFriendsList(Integer userId) {
        return null;
    }

    @Override
    public Set<User> getMutualFriends(Integer userId, Integer otherUserId) {
        return null;
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
                .build();
    }
}
