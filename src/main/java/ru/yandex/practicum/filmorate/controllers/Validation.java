package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
@RequiredArgsConstructor
@Slf4j
@Component
public class Validation {
    public void validation(Film film) {
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
        if (!StringUtils.hasLength(String.valueOf(film.getDuration()))
                || film.getDuration() < 0) {
            log.error("Продолжительность должна быть положительной {}", film.getDuration());
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (film.getReleaseDate() == null
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указана дата до 28 декабря 1895 {}", film.getReleaseDate());
            throw new ValidationException("Проверьте дату, указана дата до 28 декабря 1895 или забыли указать дату выхода фильма");
        }
        log.info("Film is valid");
    }

    public void validation(User user) {
        log.info("Валидация Пользователя");
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
        log.info("User is valid {} ", user.getEmail());
    }
}
