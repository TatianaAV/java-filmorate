package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class DbFilmStorageTest {

    private final DbFilmStorage filmStorage;

    @Test
    @Sql(scripts = {"file:src/test/java/ru/yandex/practicum/filmorate/resources/sql/dataFilm.sql"})
    void getAll() {
        log.info("\n test 1 films {}", filmStorage.getAll().size());
        List<Film> films = filmStorage.getAll();
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(films.get(2)).hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    void updateFilm() {
        log.info("\n test 2 films {}", filmStorage.getAll().size());
        Film film = new Film(1, "Бездна",
                "radicalising",
                LocalDate.of(1967, 3, 25),
                100, 0);
        film = filmStorage.updateFilm(film);
        assertEquals(Optional.of(film), filmStorage.getFilmById(1));
    }

    @Test

    void getPopular() {
        log.info("\n test 3 films {}", filmStorage.getAll().size());
        List<Film> films = filmStorage.getPopular(10);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(films.get(2)).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
      void getFilmById() {
        log.info("\n test 4 films {}", filmStorage.getAll().size());
        Optional<Film> filmOptional = filmStorage.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }


    @Test
    void createFilm() {
        log.info("\n test 5 films {}", filmStorage.getAll().size());
        Film film = new Film("Бездна",
                "radicalising",
                LocalDate.of(1967, 3, 25),
                100, 0);
        film = filmStorage.createFilm(film);
        assertEquals(Optional.of(film), filmStorage.getFilmById(1));
        assertEquals(filmStorage.getAll().size(), 4);
    }
}
