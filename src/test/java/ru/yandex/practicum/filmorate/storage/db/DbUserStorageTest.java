package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class DbUserStorageTest {

    private final DbUserStorage userStorage;

    @Test
    @Sql(scripts = {"file:src/test/java/ru/yandex/practicum/filmorate/resources/sql/dataUser.sql"})
    void getAll() {
        log.info("\n test 1 users {}", userStorage.getAll().size());
        List<User> users = userStorage.getAll();
        assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(users.get(2)).hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    void updateUser() {
        log.info(" \n test 2 users {}", userStorage.getAll().size());
        User user = new User(1,
                "ivashka@ya.ru",
                "ИВАНЫЧ",
                "Иванов Иван Иванович",
                LocalDate.of(1967, 3, 25));
        user = userStorage.updateUser(user);
        assertEquals(Optional.of(user), userStorage.getUserById(1));
    }

    @Test
    void createUser() {
        log.info("\n test 3 users {}", userStorage.getAll().size());
        User user = new User(
                "radicalising",
                "Иванов Иван Иванович",
                "iva@ya.ru",
                LocalDate.of(1967, 3, 25));
        user = userStorage.createUser(user);
        assertEquals(Optional.of(user), userStorage.getUserById(4));
    }

    @Test
    void deleteUser() {
        log.info("\n test 4 users {}", userStorage.getAll().size());
        assertEquals(userStorage.getAll().size(), 4);
        userStorage.deleteUser(new User(1, "ivashka@ya.ru", "ИВАНЫЧ", "Иванов Иван Иванович", LocalDate.of(1970, 04, 20)));
        assertEquals(userStorage.getAll().size(), 3);

    }


}