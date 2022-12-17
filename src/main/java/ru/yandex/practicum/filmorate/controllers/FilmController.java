package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class FilmController {

    private final FilmService filmService;

    @PostMapping("/films")
    public Film
    create(@RequestBody
           Film film) {
        log.info("\n == PostMapping: Фильм  {} ==", film.getName());
        filmService.createFilm(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        log.info("\n == GetMapping: Список всех фильмов ==");
        return filmService.getAll();
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("\n == PutMapping: Фильм с id {} {} ==", film.getName(), film.getGenres());
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping(value = "/films/{id}")
    public Optional<Film> getFilmById(@PathVariable("id") long filmId) {
        log.info("\n == GetMapping: Фильм с id {}==", filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/mpa")
    public Collection<MPA> getAllMpa() {
        log.info("\n == GetMapping: AllMpa==");
        return filmService.getMpa();
    }

    @GetMapping("/mpa/{id}")
        public MPA getByIdMpa(@PathVariable("id") int id) {
        log.info("\n == GetMapping: Mpa Id==");
        return filmService.getByIdMpa(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres() {
        log.info("\n == GetMapping: AllGenres==");
        return filmService.getGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getByIdGenre(@PathVariable("id") int id) {
        log.info("\n == GetMapping: Genre Id==");
        return filmService.getByIdGenre(id);
    }

    //GET /films/popular?count={count} — возвращает список из первых count популярных фильмов
    @GetMapping(value = "/films/popular")
    public List<Film> getPopular(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        log.info("\n == GetMapping: Популярные фильмы список из {} ==", count);
        return filmService.getPopular(count);
    }

    //PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.

 @PutMapping("/films/{id}/like/{userId}")
    public void createLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("\n == PutMapping: Like user {} с film {}==", userId, id);
        filmService.addLike(id, userId);
    }

    //DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeInFilm(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        log.info("\n == DeleteMapping: у Фильма с id {} удален лайк {} ==", filmId, userId);
        filmService.removeLike(filmId, userId);
    }
}
