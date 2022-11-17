package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserStorage userStorage;

    public List<User> getFriendsOfUser(Long userId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUsers().get(userId);
        for (long id : user.getFriends()) {
            if (userStorage.getUsers().containsKey(id)) {
                friends.add(userStorage.getUsers().get(id));
            }
        }
        log.info("Cписок друзей пользователя {} получен", userId);
        return friends;
    }

    public User deleteFriendsOfUser(Long userId, Long friendId) {
        if (!userStorage.getUsers().containsKey(userId) || !userStorage.getUsers().containsKey(friendId)) {
            log.error("Пользователь не найден {} или {}", userId, friendId);
            throw new FilmNotFoundException("Пользователь " +
                    userId + ", " + friendId + " не найден.");
        }
        User user = userStorage.getUsers().get(userId);
        user.getFriends().removeIf(id -> friendId == user.getId());
        User friend = userStorage.getUsers().get(friendId);
        friend.getFriends().removeIf(id -> userId == friend.getId());
        log.info("юзер {} удален из списка друзей {} ", friendId, userId);
        return user;
    }

    public User postFriend(Long id, Long friendId) {
        if (!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(friendId)) {
            log.error("Пользователь не найден {} или {}", id, friendId);
            throw new FilmNotFoundException("Пользователь " +
                    id + ", " + friendId + " не найден.");
        }
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        userStorage.getUsers().put(id, user);
        userStorage.getUsers().put(friendId, friend);
        log.info("Добавлен в друзья {} ", friendId);
        return user;
    }

    public List<User> getListOfFriendsAssociatedWithOtherUsers(Long userId, Long otherId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUsers().get(userId);
        User otherUser = userStorage.getUsers().get(otherId);
        for (long id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                friends.add(userStorage.getUsers().get(id));
            }
        }
        log.info("Cписок друзей пользователя {} общих с {} получен", userId, otherId);
        return friends;
    }
}
