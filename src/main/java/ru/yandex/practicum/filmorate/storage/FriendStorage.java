package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void removeFriends(long userId, long friendId);

    void addFriend(long userId, long friendId);


    List<User> getCommonFriends(long userId, long otherId);

    List<User> getFriendsOfUser(long userId);
}
