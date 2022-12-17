package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage{

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(long filmId);

    List<Film> getAll();

    List<Film> getPopular(int count)/*
        // выполняем запрос к базе данных.
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films  where  film_id = ?", filmId);
        // обрабатываем результат выполнения запроса
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getLong("film_id"),
                    filmRows.getString("name_film"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getLong("duration")
            );



                   log.info("Найден film: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Film с идентификатором {} не найден.", filmId);
            throw new FilmNotFoundException("Film  " +
                    filmId + " не найден.");
        }
    }

*/;
}
