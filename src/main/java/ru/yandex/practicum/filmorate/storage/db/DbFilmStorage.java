package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        log.info("Обращение в DbfilmStorage createFilm");
        try {
            String sql = "INSERT INTO public.films (NAME_FILM, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)" +
                    "values (?,?,?,?,?,?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            log.info("DbfilmStorage insert sql");
            jdbcTemplate.update(connection -> {
                PreparedStatement stm = connection.prepareStatement(sql, new String[]{"film_id"});
                stm.setString(1, film.getName());
                stm.setString(2, film.getDescription());
                stm.setLong(4, film.getDuration());
                stm.setInt(5, film.getRate());
                if (film.getMpa() != null && film.getMpa().getId() > 0) {
                    stm.setInt(6, film.getMpa().getId());
                }
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
        } catch (DataAccessException e) {
            throw new UserAlreadyExistException("Фильм с таким описанием уже существует" + e.getCause());
        }
        log.info("POST Film добавлен: {}  {}", film.getName(), film.getReleaseDate());
        return film;
    }

    @Override
    public Film update(Film film) {
        log.info("DbFilmStorage updateFilm(Film film) {}", film);
        String sql = "update films set NAME_FILM = ?, description = ?," +
                " RELEASE_DATE = ?, duration = ?, MPA_ID = ? where FILM_ID = ?";
        int i = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (i != 1) {
            throw new FilmNotFoundException("Not Found film id = " + film.getId());
        }
        saveGenre(film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        log.info("DbFilmStorage findAll()");
        String sql = "select film_id, name_film, description, release_date, duration, rate, F.MPA_ID as MPA_ID, M.NAME as NAME" +
                "                 from FILMS F" +
                "               left join MPA M on F.MPA_ID = M.MPA_ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        loadFilmsGenres(films);
        return films;
    }

    @Override
    public Film getById(long filmId) {

        final String sql = "select film_id, name_film, description, release_date, duration, rate, " +
                "                  F.MPA_ID as MPA_ID, M.NAME as NAME from FILMS F " +
                "           left join MPA M on F.MPA_ID = M.MPA_ID  where  film_id = ?";
        final List<Film> films =
                jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmId);
        if (films.size() != 1) {
            throw new FilmNotFoundException("Not Found film id = " + filmId);
        }
        log.info("filmStorage getFilmById(long filmId)");
        loadFilmsGenres(films);
        return films.get(0);
    }

    @Override
    public List<Film> getPopular(int count) {

        String sql = "select film_id, name_film, description, release_date, duration, rate, " +
                "            F.MPA_ID as MPA_ID, M.NAME as NAME" +
                "     from FILMS F " +
                "     left join MPA M on F.MPA_ID = M.MPA_ID " +
                "     order by RATE desc limit ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
        loadFilmsGenres(films);
        return films;
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
        film.setMpa(
                new MPA(resultSet.getInt("mpa_id"),
                        resultSet.getString("name")));
        return film;
    }

    private void loadFilmsGenres(List<Film> films) throws DataAccessException {
        final List<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        jdbcTemplate.query(
                String.format("select FILM_ID, G.* from GENRE G " +
                        "                        left join FILM_GENRE FG on G.GENRE_ID = FG.GENRE_ID " +
                        "                        where FILM_ID in (%s)", inSql),
                ids.toArray(),
                (rs, rowNum) -> makeFilmList(rs, films));
    }

    private Film makeFilmList(ResultSet rs, List<Film> films) throws SQLException {
        long filmId = rs.getLong("film_id");
        int genreId = rs.getInt("genre_id");
        String name = rs.getString("name");
        final Map<Long, Film> filmMap = films.stream().collect(Collectors.toMap(Film::getId, film -> film));
        filmMap.get(filmId).addGenre(new Genre(genreId, name));
        return filmMap.get(filmId);
    }


    private void saveGenre(Film film) {
        final Long filmId = film.getId();
        jdbcTemplate.update(
                "DELETE FROM FILM_GENRE WHERE FILM_ID = ?",
                filmId);
        final TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        if (film.getGenres() == null) {
            film.setGenres(genres);//пустой сет
            return;
        }
        genres.addAll(film.getGenres());
        film.setGenres(genres);
        final List<Genre> genresList = new ArrayList<>(film.getGenres());
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

