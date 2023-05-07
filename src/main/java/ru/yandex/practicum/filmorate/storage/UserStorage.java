package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User findUser(Integer userId);

    User addUser(User user);

    Collection<User> getAllUsers();

    User updateUser(User user);

    void resetIdNumberSeq();
}
