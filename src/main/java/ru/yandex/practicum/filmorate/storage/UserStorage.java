package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User create(User user);

    User update(User user);

    void delete(User user);

    User getById(long id);

    List<User> getFriendsOfUser(long userId);

    List<User> getCommonFriends(long userId, long otherId);
}
