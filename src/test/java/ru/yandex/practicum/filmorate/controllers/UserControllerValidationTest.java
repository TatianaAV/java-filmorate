package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerValidationTest {
    Validation validation = new Validation();

    @Test
    void testUserExceptionEmailNull() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            User user = new User(null,
                                    "JohnDow", "John Dow",
                                    LocalDate.of(1945, 8, 25));
                            validation.validation(user);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testUserExceptionEmailIsBlank() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            User user = new User("      ", "JohnDow", "John Dow", LocalDate.of(1945, 8, 25));
                            validation.validation(user);
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testUserExceptionEmailNotAt() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> validation.validation(new User("yandex.ru",
                                "JohnDow", "John Dow", LocalDate.of(1945, 8, 25))));
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testUserExceptionLoginNull() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            validation.validation(new User("mail@yandex.ru", null, "John Dow", LocalDate.of(1945, 8, 25)));
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testUserExceptionLoginIsBlank() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            validation.validation(new User("yandex@mail.ru", " ",
                                    null,
                                    LocalDate.of(1945, 8, 25)));
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

    @Test
    void testUserExceptionLoginIsSpace() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            validation.validation(new User("yandex@mail.ru", "dolore ullamco",
                                    null,
                                    LocalDate.of(1945, 8, 25)));
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }

   /* @Test
    void testUserExceptionLNameNull() {
        User user = new User("yandex@mail.ru", "doloreUllamco",
                null,
                LocalDate.of(1945, 8, 25));
        User user1 = usereService.validation(user);

        Assertions.assertEquals(user.getLogin(), user1.getName());
    }*/

    @Test
    void testUserExceptionDateOfBirthday() {
        ValidationException exception =
                assertThrows(
                        ValidationException.class, () -> {
                            validation.validation(new User("yandex@mail.ru", "doloreUllamco",
                                    null,
                                    LocalDate.of(2458, 8, 25)));
                        });
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertFalse(exception.getMessage().isBlank());
    }


}