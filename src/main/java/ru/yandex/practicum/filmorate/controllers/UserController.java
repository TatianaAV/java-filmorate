package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        log.info("\n == GetMapping: список users ==");
        return userService.findAll();
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        log.info("\n == PostMapping: createUser {} ==", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("\n == PutMapping:  {} ==", user.getLogin());
        return userService.updateUser(user);
    }

    @GetMapping(value = "/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long userId) {
        log.info("\n == GetMapping: пользователь {} ==", userId);
        return userService.getUserById(userId);
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getListOfFriendsAssociatedWithOtherUsers(@PathVariable("id") Long userId,
                                                               @PathVariable("otherId") Long otherId) {
        log.info("\n == GetMapping: список друзей общих с другими пользователями {} ==", userId + ", " + otherId);
        return userService.getCommonFriends(userId, otherId);
    }


    // GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @GetMapping(value = "/{id}/friends")
    public List<User> getFriendsOfUser(@PathVariable("id") Long userId) {
        log.info("\n == GetMapping: список друзей юзера {} ==", userId);
        return userService.getFriendsOfUser(userId);
    }

    // PUT /users/{id}/friends/{friendId} — добавление в друзья.
    @PutMapping(value = "/{id}/friends/{friendId}")
    public void postFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        log.info("\n == PutMapping: добавление в друзья {} ==", userId + " friend " + friendId);
        userService.postFriend(userId, friendId);
    }

    //  DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriendsOfUser(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("\n == DeleteMapping: удаление из друзей {} ==", userId + " friend " + friendId);
        userService.deleteFriendsOfUser(userId, friendId);
    }

    @DeleteMapping
    public void delete(@RequestBody User user) {
        log.info("\n == DeleteMapping:  {} ==", user.getLogin());
       userService.deleteUser(user);
    }
}

