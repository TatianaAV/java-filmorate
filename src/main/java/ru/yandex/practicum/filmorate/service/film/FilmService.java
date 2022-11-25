package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    public List<Film> findAll() {
        log.info("Обращение в filmStorage");
        return filmStorage.findAll();
    }

    public List<Film> findAllSorted(Integer count) {
      return filmStorage.sortedFilm(count);
    }

    public Film createLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.addLikes(user.getId());
        filmStorage.updateFilm(film);
        log.info("Фильму  {}, поставил лайк пользователь {}", filmId, userId);
        return film;
    }

    public Film deleteLikeInFilm(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.delLikes(user.getId());
        filmStorage.updateFilm(film);
        log.info("Пользователь  {}, удалил лайк у фильма {}", userId, filmId);
        return film;
    }

    public Film getFilmById(Long filmId) {
        log.info("Обращение в filmStorage");
        return filmStorage.getFilmById(filmId);
    }

    public Film createFilm(Film film) {
        log.info("Обращение в filmStorage");
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обращение в filmStorage");
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Film film) {
        log.info("Обращение в filmStorage");
        filmStorage.deleteFilm(film);
    }
}
