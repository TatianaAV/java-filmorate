package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.db.DbUserStorage.makeUser;


@Slf4j
@RequiredArgsConstructor
@Component
public class DbFriendStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getFriendsOfUser(long userId) {
        log.info(" DbFriendStorage Получить друзей  юзера  {} ", userId);
        String sql = "select * " +
                " from USERS U " +
                " join FRIENDS F on F.FRIEND_ID = U.USER_ID " +
                " where F.USER_ID = ? " +
                " order by U.USER_ID";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
        log.info("Друзья  юзера  {} ", users);
        return users;
    }

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

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        log.info(" DbFriendStorage getCommonFriends общих друзей друга {}  юзера  {} ", otherId, userId);
        String sql = "SELECT U.USER_ID, USER_NAME, EMAIL,LOGIN,BIRTHDAY FROM USERS as U" +
                " inner join FRIENDS F on U.USER_ID = F.FRIEND_ID" +
                " INNER JOIN friends AS f2 ON f.friend_id = f2.friend_id" +
                " WHERE f.user_id = ? AND f2.user_id = ?";
        log.info("Cписок общих друзей пользователя {} и  {} ", userId, otherId);

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, otherId);
    }
}