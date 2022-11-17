package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    //GET /films/popular?count={count} — возвращает список из первых count
    @GetMapping(value = "/popular")
    public List<Film> findAllSorted(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        log.info("== GetMapping: Популярные фильмы список из {} ==", count);
        return filmService.findAllSorted(count);
    }

    //PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping(value = "/{id}/like/{userId}")
    public Film createLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("== PutMapping: Like user {} с film {}==", userId, id);
        return filmService.createLike(id, userId);
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable("id") Long filmId) {
        log.info("== GetMapping: Фильм с id {}==", filmId);
        return filmStorage.getFilmById(filmId);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("==PostMapping: Фильм  {}==", film.getName());
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("==PutMapping: Фильм с id {}==", film.getName());
        return filmStorage.updateFilm(film);
    }

    //DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film deleteLikeInFilm(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        log.info("== DeleteMapping: у Фильма с id {} удален лайк {} ==", filmId, userId);
        return filmService.deleteLikeInFilm(filmId, userId);
    }

    @DeleteMapping
    public boolean deleteFilm(Film film) {
        log.info("== DeleteMapping: Фильм с id {} удален ==", film.getId());
        return filmStorage.deleteFilm(film);
    }
}
