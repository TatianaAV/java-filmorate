package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbUserStorageTest {
    private final UserStorage userStorage;
    private User user1;
    private User user2;

    @BeforeEach
    void setup() {
        user1 = new User();
        user1.setName("Иванов Иван Иванович");
        user1.setEmail("iva@ya.ru");
        user1.setLogin("iva");
        user1.setBirthday(LocalDate.of(1970, 04, 20));

        user2 = new User();
        user2.setName("Смирнов Сергей Сергеевич");
        user2.setEmail("smir@ya.ru");
        user2.setLogin("smir");
        user2.setBirthday(LocalDate.of(1975, 05, 25));
    }

    @AfterEach
    void clear() {
        for (User user : userStorage.getAll()) {
            userStorage.delete(user);
        }
    }

    @Test
    void getAllUsersTest() {
        log.info("\n test 1 users getAll {}", userStorage.getAll().size());
        userStorage.create(user1);
        userStorage.create(user2);

        assertEquals(2, userStorage.getAll().size());
    }

    @Test
    void updateUserTest() {
        log.info(" \n test 2 users update {}", userStorage.getAll().size() + "\n");
        userStorage.create(user1);
        user1.setName("Обновление имени");
        user1.setEmail("iva@ya.ru");
        userStorage.update(user1);

        assertEquals("Обновление имени", userStorage.getById(user1.getId()).getName());
        assertEquals("iva@ya.ru", userStorage.getById(user1.getId()).getEmail());
    }

    @Test
    void getUserByIdTest() {
        userStorage.create(user1);

        assertEquals(user1.getId(), userStorage.getById(user1.getId()).getId());
        assertEquals(user1.getEmail(), userStorage.getById(user1.getId()).getEmail());
        assertEquals(user1.getLogin(), userStorage.getById(user1.getId()).getLogin());
        assertEquals(user1.getName(), userStorage.getById(user1.getId()).getName());
        assertEquals(user1.getBirthday(), userStorage.getById(user1.getId()).getBirthday());
    }


    @Test
    void deleteUserTest() {
        log.info("\n test 4 users {}", userStorage.getAll().size());
        userStorage.create(user1);
        userStorage.delete(user1);

        assertThrows(UserNotFoundException.class, () -> userStorage.getById(user1.getId()));
    }
}
