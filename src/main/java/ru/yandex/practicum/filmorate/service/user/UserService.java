package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.Validation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final Validation validation;
    private final UserStorage userStorage;
    private final FriendStorage friendsStorage;

    public UserService(Validation validation, UserStorage userStorage, FriendStorage friendsStorage) {
        this.validation = validation;
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public List<User> getAll() {
        log.info("Обращение в userStorage List<User> findAll()");
        return userStorage.getAll();
    }

    public User create(User user) {
        validation.validation(user);
        log.info("Обращение в userStorage createUser(User user)");
        return userStorage.create(user);
    }

    public User update(User user) {
        validation.validation(user);
        log.info("Обращение в userStorage");
        return userStorage.update(user);
    }

    public User getById(Long userId) {
        log.info("Обращение в userStorage");
        return userStorage.getById(userId);
    }

    public void delete(User user) {
        log.info("Обращение в userStorage");
        userStorage.delete(user);
    }

    public List<User> getFriendsOfUser(Long userId) {
        log.info(" список друзей {} ", userId);
        return userStorage.getFriendsOfUser(userId);
    }

    public void deleteFriendsOfUser(Long userId, Long friendId) {
        log.info("юзер {} удален из списка друзей {} ", friendId, userId);
        friendsStorage.removeFriends(userId, friendId);
    }

    public void postFriend(Long id, Long friendId) {
        log.info("Добавлен в друзья {} ", friendId);
        friendsStorage.postFriend(id, friendId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Получить друзей {} юзера  {} ", otherId, userId);
        return userStorage.getCommonFriends(userId, otherId);
    }
}
