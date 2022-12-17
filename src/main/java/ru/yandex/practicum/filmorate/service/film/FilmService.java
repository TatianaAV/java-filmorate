package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.Validation;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.db.DbFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    private final Validation validation;
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public FilmService(Validation validation, DbFilmStorage filmStorage,
                       LikesStorage likesStorage,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage) {
        this.validation = validation;
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public Film createFilm(Film film) {
        validation.validation(film);
        log.info("Обращение в filmStorage");
        filmStorage.createFilm(film);
        return film;
    }


    public List<Film> getAll() {
        log.info("Обращение в DbfilmStorage");
        return filmStorage.getAll();
    }

    public Film updateFilm(Film film) {
        validation.validation(film);
        log.info("Обращение в filmStorage");
        filmStorage.updateFilm(film);
        return film;
    }

    public Optional<Film> getFilmById(Long filmId) {
        log.info("Обращение в filmStorage");
        return filmStorage.getFilmById(filmId);
    }

    public MPA getByIdMpa(int id) {
        log.info("Обращение в mpaStorage");
        return mpaStorage.getById(id);
    }

    public Collection<MPA> getMpa() {
        log.info("Обращение в mpaStorage");
        return mpaStorage.getAll();
    }

    public Collection<Genre> getGenre() {
        log.info("Обращение в genreStorage");
        return genreStorage.getAll();
    }

    public Genre getByIdGenre(int id) {
        log.info("Обращение в genreStorage");
        return genreStorage.getGenre(id);
    }

    public List<Film> getPopular(int count) {
        log.info("Обращение в filmStorage");
        return filmStorage.getPopular(count);
    }

    public void addLike(long filmId, long userId) {
        log.info("Обращение в filmStorage");
        likesStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.info("Обращение в filmStorage");
        likesStorage.removeLike(filmId, userId);
    }
}
