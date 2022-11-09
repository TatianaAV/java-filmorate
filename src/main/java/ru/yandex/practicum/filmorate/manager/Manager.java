package ru.yandex.practicum.filmorate.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Manager {
    public Film validationFilm(Film film) {
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
        return film;
    }

    public User validationUser(User user) {
        if (user.getEmail() == null
                || user.getEmail().isBlank()
                || user.getEmail().lastIndexOf("@") < 0) {
            log.error("Проверьте email {}", user.getEmail());
            throw new ValidationException("Проверьте адрес электронной почты." + user.getEmail());
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Проверьте Login {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может пустой или быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
