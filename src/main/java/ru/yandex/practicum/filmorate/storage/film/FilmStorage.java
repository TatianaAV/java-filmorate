package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    List<Film> findAll();

    Film getFilmById(Long filmId);

    List<Film> sortedFilm(Integer count);
}
