package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.Validation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService  {

    private final Validation validation;
    private final UserStorage userStorage;
    private final FriendStorage friendsStorage;


    public UserService(Validation validation, UserStorage userStorage, FriendStorage friendsStorage) {
        this.validation = validation;
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public List<User> findAll() {
        log.info("Обращение в userStorage List<User> findAll()");
        return userStorage.getAll();
    }

    public User createUser(User user) {
        validation.validation(user);
        log.info("Обращение в userStorage createUser(User user)");
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validation.validation(user);
        log.info("Обращение в userStorage");
        return userStorage.updateUser(user);
    }

    public Optional<User> getUserById(Long userId) {
        log.info("Обращение в userStorage");
        return userStorage.getUserById(userId);
    }

    public void deleteUser(User user) {
        log.info("Обращение в userStorage");
        userStorage.deleteUser(user);
    }


    public List<User> getFriendsOfUser(Long userId) {
        log.info(" список друзей {} ",  userId);
        return friendsStorage.getFriendsOfUser(userId);
    }

    public void deleteFriendsOfUser(Long userId, Long friendId) {
        log.info("юзер {} удален из списка друзей {} ", friendId, userId);
        friendsStorage.removeFriends(userId, friendId);
    }

    public void postFriend(Long id, Long friendId) {
        log.info("Добавлен в друзья {} ", friendId);
        friendsStorage.addFriend(id, friendId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Получить друзей {} юзера  {} ", otherId, userId);
        return friendsStorage.getCommonFriends(userId, otherId);
    }

}
