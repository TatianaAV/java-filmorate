package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.LikesStorage;


@Slf4j
@Component
public class DbLikesStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbLikesStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "merge into likes (film_id, users_id) values (?, ?)";

        jdbcTemplate.update(sql, filmId, userId);
        log.info("Фильму  {}, поставил лайк пользователь {}", filmId, userId);
        updateRate();
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sql = "delete from likes where film_id = ? and users_id = ?";
        log.info("Пользователь  {}, удалил лайк у фильма {}", userId, filmId);
        int row = jdbcTemplate.update(sql, filmId, userId);
        if(row!=1){
            throw  new ObjectNotFoundException("Фильм  s% или Пользователь s% не существуют" + filmId + userId);}
        updateRate();
    }

    private void updateRate() {
        String sql = "update films f set rate = (select count(l.USERS_ID) from likes l where l.film_id = f.FILM_ID);";
        jdbcTemplate.update(sql);
    }
}

