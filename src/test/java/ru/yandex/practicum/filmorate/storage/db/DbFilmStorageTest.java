package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class DbFilmStorageTest {

    private final FilmStorage filmStorage;

    private Film film;

    @BeforeEach
    void setup() {
        film = new Film();
        film.setName("Бездна");
        film.setDescription("Погружение в мариинскую впадину");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(2022, 12, 21));
        film.setMpa(new MPA(1, null));
        film.setGenres(null);
    }

    @Test
    void addFilmTest() {
        filmStorage.create(film);
        List<Film> films = filmStorage.getAll();

        assertEquals(1, film.getId());
       assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void updateFilmTest() {
        filmStorage.create(film);
        film.setName("Бездна_1");
        film.setDescription("Первое погружение");
        filmStorage.update(film);
        Film newFilm = filmStorage.getById(film.getId());

        assertEquals("Бездна_1", newFilm.getName());
        assertEquals("Первое погружение", newFilm.getDescription());
    }
}