package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static int idNumberSeq = 0;
    private final Map<Integer, User> usersMap = new HashMap<>();

    public Collection<User> getAllUsers() {
        return usersMap.values();
    }

    public User addUser(User user) {
        UserValidator.validate(user);

        if (Objects.isNull(user.getName())
                || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        usersMap.put(user.getId(), user);

        log.info("User {} added successfully", user);

        return user;
    }

    public User updateUser(User user) {
        UserValidator.validate(user);

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);

            log.info("User {} added successfully", user);
        } else {
            log.warn("User with id {} wasn't found", user.getId());

            throw new NullPointerException("пользователь с Id = " + user.getId() + " не найден");
        }

        return user;
    }

    public User findUser(Integer userId) {
        User user = usersMap.get(userId);

        if (Objects.isNull(user)) {
            throw new NullPointerException("Пользователь с Id = " + userId + " не найден.");
        }

        return user;
    }

    private static int getNextId() {
        idNumberSeq++;

        return idNumberSeq;
    }

    public void resetIdNumberSeq() {
        idNumberSeq = 0;
    }
}
