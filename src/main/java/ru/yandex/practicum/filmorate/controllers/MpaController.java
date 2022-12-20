package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<MPA> getAllMpa() {
        log.info("\n == GetMapping: AllMpa==");
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public MPA getByIdMpa(@PathVariable("id") int id) {
        log.info("\n == GetMapping: Mpa Id==");
        return mpaService.getById(id);
    }
}

