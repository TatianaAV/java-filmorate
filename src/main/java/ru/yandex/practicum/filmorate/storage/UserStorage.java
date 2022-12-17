package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> getAll();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    Optional<User> getUserById(long id);
}
