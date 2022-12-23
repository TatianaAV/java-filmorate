package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Slf4j
@RequiredArgsConstructor
@Component
public class DbFriendStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void removeFriends(long userId, long friendId) {
        log.info(" DbFriendStorage удалить друга {}  юзера  {} ", friendId, userId);
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.info("юзер {} удален из списка друзей {} ", friendId, userId);
    }

    @Override
    public void postFriend(long userId, long friendId) {
        log.info(" DbFriendStorage добавить друга {}  юзера  {} ", friendId, userId);
        try {
            String sqlQuery = "merge into friends (user_id, friend_id) values (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (DataAccessException ex) {
            throw new UserNotFoundException("Аккаунт " + friendId + " не найден " + ex.getCause());
        }
    }
}