package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerValidationTest {

    Validation validation = new Validation();

    @Test
    void testFilmExceptionNameNull() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            Film film = new Film(null,
                                    "radicalising",
                                    LocalDate.of(1967, 3, 25), 100);
                            validation.validation(film);
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
                                    "radicalising",
                                    LocalDate.of(1967, 3, 25),
                                    100);
                            validation.validation(film);
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
                            validation.validation(film);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testFilmExceptionDescriptionIsNull() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            Film film = new Film("Film name",
                                    null,
                                    LocalDate.of(1900, 3, 25),
                                    100L);
                            validation.validation(film);
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
                                    LocalDate.of(1980, 3, 25),
                                    -200);
                            validation.validation(film);
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
                                    LocalDate.of(1890, 3, 25),
                                    200);
                            validation.validation(film);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }
}

