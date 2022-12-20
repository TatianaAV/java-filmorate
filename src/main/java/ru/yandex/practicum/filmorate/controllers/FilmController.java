package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film
    create(@RequestBody
           Film film) {
        log.info("\n == PostMapping: Фильм  {} ==", film.getName());
        filmService.create(film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("\n == GetMapping: Список всех фильмов ==");
        return filmService.getAll();
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("\n == PutMapping: Фильм с id {} {} ==", film.getName(), film.getGenres());
        filmService.update(film);
        return film;
    }

    @GetMapping(value = "/{id}")
    public Film getFilmById(@PathVariable("id") long filmId) {
        log.info("\n == GetMapping: Фильм с id {}==", filmId);
        return filmService.getById(filmId);
    }

    //GET /films/popular?count={count} — возвращает список из первых count популярных фильмов
    @GetMapping(value = "/popular")
    public List<Film> getPopular(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        log.info("\n == GetMapping: Популярные фильмы список из {} ==", count);
        return filmService.getPopular(count);
    }

    //PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.

    @PutMapping("/{id}/like/{userId}")
    public void createLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("\n == PutMapping: Like user {} с film {}==", userId, id);
        filmService.addLike(id, userId);
    }

    //DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeInFilm(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        log.info("\n == DeleteMapping: у Фильма с id {} удален лайк {} ==", filmId, userId);
        filmService.removeLike(filmId, userId);
    }
}
