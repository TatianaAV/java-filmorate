package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;
    protected TreeSet<Film> popularFilm =
            new TreeSet<>(Comparator.nullsLast(Comparator.comparingInt(Film::getLikeSize)));

    public List<Film> findAllSorted(Integer count) {
        popularFilm.addAll(filmStorage.getFilms().values());
        log.info("Фильмы отсортированы, count {}", count);
        return popularFilm.descendingSet().stream().limit(count).collect(Collectors.toList());
    }

    public Film createLike(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.error("Фильм не найден {}", filmId);
            throw new FilmNotFoundException("Фильм " +
                    filmId + " не найден.");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            log.error("Пользователь не найден {}", userId);
            throw new UserNotFoundException("Пользователь " +
                    userId + " не найден.");
        }
        Film film = filmStorage.getFilms().get(filmId);
        User user = userStorage.getUsers().get(userId);
        film.getLikes().add(user.getId());
        filmStorage.getFilms().put(filmId, film);
        log.info("Фильму  {}, поставил лайк пользователь {}", filmId, userId);
        return film;
    }

    public Film deleteLikeInFilm(Long filmId, Long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.error("Фильм не найден {}", filmId);
            throw new FilmNotFoundException("Фильм " +
                    filmId + " не найден.");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            log.error("Пользователь не найден {}", userId);
            throw new UserNotFoundException("Пользователь " +
                    userId + " не найден.");
        }
        Film film = filmStorage.getFilms().get(filmId);
        User user = userStorage.getUsers().get(userId);
        film.getLikes().removeIf(id -> userId == user.getId());
        log.info("Пользователь  {}, удалил лайк у фильма {}", userId, filmId);
        return film;
    }
}
