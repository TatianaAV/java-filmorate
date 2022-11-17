package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    public HashMap<Long, User> getUsers() {
        return users;
    }

    private final HashMap<Long, User> users = new HashMap<>();
    private long generatedId = 0;

    public long generatedId() {
        ++generatedId;
        log.info("сгенерирован id {}", generatedId);
        return generatedId;
    }

    public User validationUser(User user) {
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
        return user;
    }

    @Override
    public List<User> findAll() {
        log.info("GET Текущее количество users: {}", users.values());
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        User userValid = validationUser(user);
        if (users.containsValue(user)) {
            log.error("Пользователь уже зарегистрирован {}", user.getEmail());
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        long id = generatedId();
        userValid.setId(id);
        users.put(id, userValid);
        log.info("POST users пользователь добавлен: {}", userValid.getId());
        return userValid;
    }


    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь не найден PUT {}", user.getEmail());
            throw new UserNotFoundException("Пользователь " +
                    user.getEmail() + " не найден.");
        }
        long id = user.getId();
        User validateUser = validationUser(user);
        users.put(id, validateUser);
        log.info("PUT users обновлен пользователь: {}", validateUser.getId());
        return validateUser;
    }

    @Override
    public void deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь не найден PUT {}", user.getEmail());
            throw new UserNotFoundException("Пользователь " +
                    user.getEmail() + " не найден.");
        }
        log.info("Пользователь удален {} ", user.getEmail());
       users.remove(user.getId(), user);
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            log.error("Пользователь не найден {}", userId);
            throw new UserNotFoundException("Пользователь " +
                    userId + " не найден.");
        }
        log.info("Пользователь найден {} ", userId);
        return users.get(userId);
    }
}
