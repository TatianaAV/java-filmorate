package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> getFriendsOfUser(Long userId) {
        User user = userStorage.getUserById(userId);
        log.info("Cписок друзей пользователя {} получен", userId);
        return user.getFriends().stream()
                .map(userStorage::getUserById).collect(Collectors.toList());
    }

    public User deleteFriendsOfUser(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.delFriend(user.getId());
        friend.delFriend(friend.getId());
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("юзер {} удален из списка друзей {} ", friendId, userId);
        return user;
    }

    public User postFriend(Long id, Long friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Добавлен в друзья {} ", friendId);
        return user;
    }

    public List<User> getListOfFriendsAssociatedWithOtherUsers(Long userId, Long otherId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);
        log.info("Cписок общих друзей пользователя {} и  {} ", userId, otherId);
        return user.getFriends().stream()
                .filter(u -> otherUser.getFriends().contains(u))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        log.info("Обращение в userStorage");
        return userStorage.findAll();
    }

    public User createUser(User user) {
        log.info("Обращение в userStorage");
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.info("Обращение в userStorage");
        return userStorage.updateUser(user);
    }

    public User getUserById(Long userId) {
        log.info("Обращение в userStorage");
        return userStorage.getUserById(userId);
    }

    public void deleteUser(User user) {
        log.info("Обращение в userStorage");
        userStorage.deleteUser(user);
    }
}
