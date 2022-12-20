package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    public User create(User user) {
        log.info("Обращение в DbfilmStorage createUser");
        String sql = "insert INTO public.USERS (USER_NAME, email, login, birthday)" +
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
    public User update(User user) {
        log.info("DbUserStorage updateUser(User user)");
        String sql = "update USERS set USER_NAME = ?,  LOGIN = ?, EMAIL = ?, BIRTHDAY = ? where USER_ID = ?";

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
        return getById(user.getId());
    }

    @Override
    public void delete(User user) {
        long userId = user.getId();
        int rows = jdbcTemplate.update(
                "delete from USERS where user_id = ?", userId);
        log.info("Пользователь удален {},  {} row(s) deleted  ", user.getEmail(), rows);
    }


    @Override
    public User getById(long id) {
        final List<User> users = jdbcTemplate.query(
                "select * from users where USER_ID = ?",
                (rs, rowNum) -> makeUser(rs), id);
        if (users.size() != 1) {
            throw new UserNotFoundException("Not Found film id = " + id);
        }
        log.info("filmStorage getFilmById(long filmId)");
        return users.get(0);
    }

    public static User makeUser(ResultSet userRows) throws SQLException {
        return new User(
                userRows.getLong("user_id"),
                userRows.getString("email"),
                userRows.getString("login"),
                userRows.getString("user_name"),
                userRows.getDate("birthday").toLocalDate()
        );
    }
}