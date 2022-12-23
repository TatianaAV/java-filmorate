package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.Validation;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final Validation validation;
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;

    public FilmService(Validation validation, FilmStorage filmStorage,
                       LikesStorage likesStorage) {
        this.validation = validation;
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
    }

    public Film create(Film film) {
        validation.validation(film);
        log.info("Обращение в filmStorage");
        filmStorage.create(film);
        return film;
    }

    public List<Film> getAll() {
        log.info("Обращение в DbfilmStorage");
        return filmStorage.getAll();
    }

    public Film update(Film film) {
        validation.validation(film);
        log.info("Обращение в filmStorage");
        filmStorage.update(film);
        return film;
    }

    public Film getById(Long filmId) {
        log.info("Обращение в filmStorage");
        return filmStorage.getById(filmId);
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
