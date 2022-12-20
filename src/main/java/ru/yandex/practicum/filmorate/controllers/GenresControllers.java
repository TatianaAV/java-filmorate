package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genres.GenresService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenresControllers {

    private final GenresService genresService;

    @GetMapping
    public Collection<Genre> getGenres() {
        log.info("\n == GetMapping: AllGenres==");
        return genresService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getByIdGenre(@PathVariable("id") int id) {
        log.info("\n == GetMapping: Genre Id==");
        return genresService.getById(id);
    }
}

