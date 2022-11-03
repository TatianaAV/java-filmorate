package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.manager.Manager;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    Manager valid = new Manager();

    @Test
    void testFilmExceptionNameNull() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            Film film = new Film(null,
                                    "adipisicing",
                                    LocalDate.of(1967, 03, 25),
                                    100L);
                            valid.validationFilm(film);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testFilmExceptionNameIsBlank() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            Film film = new Film(" ",
                                    "adipisicing",
                                    LocalDate.of(1967, 03, 25),
                                    100L);
                            valid.validationFilm(film);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testFilmExceptionDescriptionMax200() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            Film film = new Film("Film name",
                                    "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                                            "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им" +
                                            " деньги, а именно 20 миллионов. о Куглов, который за время " +
                                            "«своего отсутствия», стал кандидатом Коломбани.",
                                    LocalDate.of(1900, 3, 25),
                                    100L);
                            valid.validationFilm(film);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testFilmExceptionDurationIsPositive() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            Film film = new Film("Name",
                                    "Description",
                                    LocalDate.of(1980, 03, 25),
                                    -200);
                            valid.validationFilm(film);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testFilmExceptionReleaseDay() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            Film film = new Film("Name",
                                    "Description",
                                    LocalDate.of(1890, 03, 25),
                                    200);
                            valid.validationFilm(film);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testFilmValidation() {
        Film film = new Film("nisi eiusmod",
                "adipisicing",
                LocalDate.of(1967, 3, 25),
                100
        );
        Film film1 = valid.validationFilm(film);

        Assertions.assertEquals(film.getName(), film1.getName());
    }
}

