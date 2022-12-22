package ru.yandex.practicum.filmorate.storage;

public interface FriendStorage {

    void removeFriends(long userId, long friendId);

    void postFriend(long userId, long friendId);
}
