package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.manager.Manager;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;
    private final Manager valid = new Manager();

    @GetMapping
    public List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        valid.validationFilm(film);
        if (films.containsValue(film)) {
            log.error("фильм уже добавлен: {}", film.getName());
            throw new ValidationException("Фильм " +
                    film.getName() + " уже добавлен в коллекцию.");
        }
        int id = generatedId();
        film.setId(id);
        films.put(id, film);
        log.trace("Put film: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        valid.validationFilm(film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм не найден {}", film.getName());
            throw new ValidationException("Фильм " +
                    film.getName() + " не найден.");
        }
        int id = film.getId();
        films.put(id, film);
        log.info("Put film: {}", film.getName());
        return film;
    }

    public int generatedId() {
        ++generatedId;
        log.info("сгенерирован id {}", generatedId);
        return generatedId;
    }
}
