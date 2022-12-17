package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class DbMpaStorage implements MpaStorage {

    JdbcTemplate jdbcTemplate;

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MPA> getAll() {
        log.info("DbMpaStorage findAll()");
        TreeSet<MPA> mpa = new TreeSet<>(Comparator.comparing(MPA::getId));
        String sql = "select * from MPA";
        Collection<MPA> mpaList = jdbcTemplate.query(sql, (rs, nr) -> makeMPA(rs));
        mpa.addAll(mpaList);
        log.info("DbMpaStorage findAll() " + mpaList);
        return mpa;
    }

    @Override
    public MPA getById(int id) {

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA where MPA_ID =  ?", id);
        if (mpaRows.next()) {
            return new MPA(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("name"));
        } else {
            log.info("404, MPA с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("MPA  " +
                    id + " не найден.");
        }
    }

    static MPA makeMPA(ResultSet rs) throws SQLException {
        log.info("DbMpaStorage makeMpa " + rs.getString("mpa_id"));
        return new MPA(
                rs.getInt("mpa_id"),
                rs.getString("name")
        );
    }
}

