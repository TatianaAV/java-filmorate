package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

interface UserStorage {

    List<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    User getUserById(Long userId);
}