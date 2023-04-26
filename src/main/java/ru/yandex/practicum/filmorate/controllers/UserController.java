package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static int idNumberSeq = 0;
    private final Map<Integer, User> usersMap = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return usersMap.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        String userValidationResult = UserValidator.validate(user);

        if (!userValidationResult.isBlank()) {
            log.warn("User {} has validation errors: {}", user, userValidationResult);

            throw new UserValidationException(userValidationResult);
        }

        if (Objects.isNull(user.getName())
                || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        usersMap.put(user.getId(), user);

        log.info("User {} added successfully", user);

        return user;
    }

    @PutMapping
    public User updateUser(@Valid  @RequestBody User user) {
        String userValidationResult = UserValidator.validate(user);

        if (!userValidationResult.isBlank()) {
            log.warn("User {} has validation errors: {}", user, userValidationResult);

            throw new UserValidationException(userValidationResult);
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (usersMap.containsKey(user.getId())) {
            usersMap.put(user.getId(), user);

            log.info("User {} added successfully", user);
        } else {
            log.warn("User with id {} wasn't found", user.getId());

            throw new UserValidationException("пользователь с Id = " + user.getId() + " не найден");
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
