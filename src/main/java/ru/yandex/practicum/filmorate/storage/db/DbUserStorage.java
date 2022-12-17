package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> getAll() {
        log.info("GET Текущее количество users List<User> findAll()");
        String sql = "select * from users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }


    @Override
    public User createUser(User user) {
        log.info("Обращение в DbfilmStorage createUser");
        String sql = "insert INTO public.USERS (username, email, login, birthday)" +
                "values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("DbUserStorage insert sql");
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sql, new String[]{"user_id"});
            stm.setString(1, user.getName());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getLogin());
            final LocalDate birthday = user.getBirthday();

            if (birthday == null)
                stm.setNull(4, Types.DATE);
            else {
                stm.setDate(4, Date.valueOf(birthday));
            }
            return stm;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("POST users пользователь добавлен: {}", user.getId());
        return user;
    }


    @Override
    public User updateUser(User user) {
        log.info("DbUserStorage updateUser(User user)");
        String sql = "update USERS set USERNAME = ?,  LOGIN = ?, EMAIL = ?, BIRTHDAY = ? where USER_ID = ?";

        int row = jdbcTemplate.update(sql,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        if (row < 1) {
            log.info("Пользователь с идентификатором {} не найден.", user.getId());
            throw new UserNotFoundException("Пользователь  " +
                    user.getId() + " не найден.");
        }
        log.info("PUT users обновлен пользователь: {}", user.getId());
        return user;
    }

    @Override
    public void deleteUser(User user) {
        long userId = user.getId();
        String sqlQuery = "delete from USERS where user_id = ?";
        int rows = jdbcTemplate.update(sqlQuery, userId);
        log.info("Пользователь удален {},  {} row(s) deleted  ", user.getEmail(), rows);
    }


    @Override
    public Optional<User> getUserById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where USER_ID = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("username"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate()
            );
            log.info("Найден пользователь: {} {}", user.getId(), user.getEmail());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException("Пользователь  " +
                    id + " не найден.");
        }
    }

    public static User makeUser(ResultSet userRows) throws SQLException {
        return new User(
                userRows.getLong("user_id"),
                userRows.getString("email"),
                userRows.getString("login"),
                userRows.getString("username"),
                userRows.getDate("birthday").toLocalDate()
        );
    }
}
