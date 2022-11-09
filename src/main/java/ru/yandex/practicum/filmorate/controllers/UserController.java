package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.manager.Manager;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Manager managerValidation = new Manager();
    private final HashMap<Integer, User> users = new HashMap<>();
    private int generatedId = 0;

    @GetMapping
    public List<User> findAll() {
        log.info("Текущее количество users: {}", users.values());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        User userValid = managerValidation.validationUser(user);
        if (users.containsValue(user)) {
            log.error("Пользователь уже зарегистрирован {}", user.getEmail());
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        int id = generatedId();
        userValid.setId(id);
        users.put(id, userValid);
        log.info("POST users: {}", userValid.getId());
        log.info("value users: {}", users.values());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь не найден PUT {}", user.getEmail());
            throw new ValidationException("Пользователь " +
                    user.getEmail() + " не найден.");
        }
        log.trace("пробуем PUT user {}", user);
        int id = user.getId();
        log.info("извлекаем id {}", id);
        User validateUser = managerValidation.validationUser(user);
        users.put(id, validateUser);
        log.info("PUT users: {}", validateUser.getId());
        log.info("value users: {}", users.values());
        return validateUser;
    }


    public int generatedId() {
        ++generatedId;
        log.info("сгенерирован id {}", generatedId);
        return generatedId;
    }
}
