package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


@Slf4j
@Component
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int genreId) {
        String sql = "select * from genre where genre_id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, genreId);
        if (!genreRows.next()) {
            log.warn("Жанр {} не найден.", genreId);
            throw new ObjectNotFoundException("Жанр не найден");
        }
        return jdbcTemplate.queryForObject(sql, DbGenreStorage::makeGenre, genreId);
    }

    @Override
    public Collection<Genre> getAll() {
            log.info("DbGenreStorage getAll()");
            TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
            String sql = "select * from GENRE";
            Collection<Genre> genreList = jdbcTemplate.query(sql, DbGenreStorage::makeGenre);
            genres.addAll(genreList);
            log.info("DbMpaStorage findAll() " + genreList);
            return genres;
         }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}