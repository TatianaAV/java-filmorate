package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
       int row = jdbcTemplate.update(sqlQuery, userId,friendId);
        if(row < 1)
        {
            throw new UserNotFoundException("удалено {} строк = " + row);
        }
        log.info("юзер {} удален из списка друзей {} ", friendId, userId);
          }

    @Override
    public void addFriend(long userId, long friendId) {
        log.info(" DbFriendStorage добавить друга {}  юзера  {} ",friendId, userId);
        try { String sqlQuery = "insert into friends (user_id, friend_id) values (?, ?)";
            int row = jdbcTemplate.update(sqlQuery, userId, friendId);
            if(row < 1)
            {
                log.info("юзер {} добавлен в список друзей {}, добавлено {} строк ", friendId, userId, row);
            }
              }
         catch(Exception ex){
        throw new UserNotFoundException("Пользователь  не найден " + userId + " " + friendId );
    }
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        log.info(" DbFriendStorage getCommonFriends общих друзей друга {}  юзера  {} ",otherId, userId);
        String sql = "SELECT * " +
                "FROM USERS U " +
                "WHERE USER_ID IN (SELECT f1.friend_id " +
                "FROM friends AS f1 " +
                "         INNER JOIN friends AS f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?)";
        log.info("Cписок общих друзей пользователя {} и  {} ", userId, otherId);

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, otherId);}
}