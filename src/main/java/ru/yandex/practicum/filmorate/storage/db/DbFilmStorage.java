package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        log.info("Обращение в DbfilmStorage createFilm");
        String sql = "INSERT INTO public.films (NAME_FILM, DESCRIPTION, RELEASE_DATE, DURATION, RATE)" +
                "values (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("DbfilmStorage insert sql");
        jdbcTemplate.update(connection -> {
            PreparedStatement stm = connection.prepareStatement(sql, new String[]{"film_id"});
            stm.setString(1, film.getName());
            stm.setString(2, film.getDescription());
            stm.setLong(4, film.getDuration());
            stm.setInt(5, film.getRate());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null)
                stm.setNull(3, Types.DATE);
            else {
                stm.setDate(3, Date.valueOf(releaseDate));
            }
            return stm;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        saveGenre(film);
        saveMpa(film);
        log.info("POST Film добавлен: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("DbFilmStorage updateFilm(Film film) {}", film);
        String sql = "update films set NAME_FILM = ?, description = ?, RELEASE_DATE = ?, duration = ?, rate = ? where FILM_ID = ?";
        int i = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getId());

        if (i != 1) {
            throw new FilmNotFoundException("Not Found film id = " + film.getId());
        }

        saveMpa(film);
        saveGenre(film);

        return film;
    }

    @Override
    public List<Film> getAll() {
        log.info("DbFilmStorage findAll()");

        String sql = "select * from FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {

        final String sql = "select * from films  where  film_id = ?";
        final List<Film> films =
                jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmId);
        if (films.size() != 1) {
            throw new FilmNotFoundException("Not Found film id = " + filmId);
        }
        log.info("filmStorage getFilmById(long filmId)");
        Film film = films.get(0);
        film.setGenres(getGenresFilm(filmId));
        film.setMpa(getMpaFilm(filmId));
        return Optional.of(film);
    }

    @Override
    public List<Film> getPopular(int count) {

        String sql = "select * from FILMS group by FILM_ID, RATE  order by  max(RATE) desc limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        log.info("filmStorage makeFilm");
        Film film = new Film(
                resultSet.getLong("film_id"),
                resultSet.getString("name_film"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getLong("duration"),
                resultSet.getInt("rate")
        );
        if (getMpaFilm(film.getId()) != null) {
            film.setMpa(getMpaFilm(film.getId()));
        }
        if (getGenresFilm(film.getId()).size() > 1 || !getGenresFilm(film.getId()).isEmpty()) {
            film.setGenres(getGenresFilm(film.getId()));
        }
        return film;
    }

    private MPA getMpaFilm(long filmId) {

        String sqlMpa = "select * from FILM_MPA FM " +
                "join MPA M on M.MPA_ID = FM.MPA_ID " +
                "where FILM_ID = ? order by M.MPA_ID";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlMpa, filmId);
        if (rs.next()) {
            return   new MPA(rs.getInt("mpa_id"), rs.getString("name"));
        }
        return null;
    }

    private void saveMpa(Film film) {
        log.info("DbFilmStorage saveMpa");
        int row = jdbcTemplate.update("delete from FILM_MPA where FILM_ID = ?", film.getId());
        final MPA mpa = film.getMpa();
        if (mpa == null || row < 0) {//&&
            return;
        }
        jdbcTemplate.batchUpdate(
                "insert into FILM_MPA (film_id, MPA_ID) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, film.getId());
                        ps.setInt(2, mpa.getId());
                    }

                    public int getBatchSize() {
                        return mpa.getId();
                    }
                }
        );
    }


    private TreeSet<Genre> getGenresFilm(long filmId) {
        TreeSet<Genre> genresFilm = new TreeSet<>(Comparator.comparing(Genre::getId));
        SqlRowSet rs = jdbcTemplate.queryForRowSet(
                "select FILM_ID, G.GENRE_ID, G.NAME from GENRE G " +
                        "left join FILM_GENRE FG on G.GENRE_ID = FG.GENRE_ID " +
                        "where FILM_ID = ? order by G.GENRE_ID", filmId);
        while (rs.next()) {
            genresFilm.add(makeGenre(rs));
        }
        return genresFilm;
    }

    static Genre makeGenre(SqlRowSet rs){
       return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

    private void saveGenre(Film film) {
        final Long filmId = film.getId();
        final TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        genres.addAll(film.getGenres());
        final int row = jdbcTemplate.update("delete from FILM_GENRE where FILM_ID = ?", film.getId());
        if (genres.size() == 0 && row < 1) {
            return;
        }
        film.setGenres(genres);
        final List<Genre> genresList = new LinkedList<>(genres);
        jdbcTemplate.batchUpdate(
                "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genresList.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genresList.size();
                    }
                });
        log.info("DbfilmStorage saveGenre {}", film);
    }
}

