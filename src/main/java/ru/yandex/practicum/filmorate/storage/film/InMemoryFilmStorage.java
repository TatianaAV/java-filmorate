package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected TreeSet<Film> popularFilm =
            new TreeSet<>(Comparator.nullsLast(Comparator.comparingInt(Film::getLikeSize)));

    private final HashMap<Long, Film> films = new HashMap<>();
    private long generatedId = 0;

    public long generatedId() {
        ++generatedId;
        log.info("сгенерирован id {}", generatedId);
        return generatedId;
    }

    public Film validationFilm(Film film) {
        log.info("Валидация фильма {}", film);
        if (film.getName() == null
                || film.getName().isBlank()) {
            log.error("Проверьте name {}", film.getName());
            throw new ValidationException("Название фильма не должно быть пустым." + film.getName());
        }
        if (film.getDescription() == null
                || film.getDescription().length() > 200) {
            log.error("Напишите что-нибудь, но не больше 200 символов {}", film.getName());
            throw new ValidationException("Напишите что-нибудь, но не больше 200 символов");
        }
        if (!StringUtils.hasLength(String.valueOf(film.getDuration()))//long не может быть null
                || film.getDuration() < 0) {
            //long duration автоматически подставляет 0, всё равно проверять на null?
            log.error("Продолжительность должна быть положительной {}", film.getDuration());
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (film.getReleaseDate() == null
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указана дата до 28 декабря 1895 {}", film.getReleaseDate());
            throw new ValidationException("Проверьте дату, указана дата до 28 декабря 1895 или забыли указать дату выхода фильма");
        }
        log.info("Film is valid");
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        validationFilm(film);
        if (films.containsValue(film)) {
            log.error("фильм уже добавлен: {}", film.getName());
            throw new UserAlreadyExistException("Фильм " +
                    film.getName() + " уже добавлен в коллекцию.");
        }
        long id = generatedId();
        film.setId(id);
        films.put(id, film);
        log.info("Фильм добавлен в коллекцию: {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validationFilm(film);
        if (!films.containsKey(film.getId())) {
            log.error("Фильм не найден {}", film.getName());
            throw new FilmNotFoundException("Фильм " +
                    film.getName() + " не найден.");
        }
        long id = film.getId();
        films.put(id, film);
        log.info("Фильм обновлен: {}", film.getName());
        return film;
    }

    @Override
    public List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> sortedFilm(Integer count) {
        popularFilm.addAll(films.values());
        log.info("Фильмы отсортированы, count {}", count);
        return popularFilm
                .descendingSet()
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильм не найден {}", filmId);
            throw new FilmNotFoundException("Фильм " +
                    filmId + " не найден.");
        }
        log.info("Фильм найден {}", filmId);
        return films.get(filmId);
    }

    @Override
    public void deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм не найден {}", film.getName());
            throw new FilmNotFoundException("Фильм " +
                    film.getName() + " не найден.");
        }
        log.info("Фильм  {} удален из коллекции", film.getName());
        films.remove(film.getId());
    }
}