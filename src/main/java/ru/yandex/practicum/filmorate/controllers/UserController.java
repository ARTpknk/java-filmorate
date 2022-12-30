package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class UserController {
    protected UserService userService;
    protected UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping("/users")
    public Collection<User> findAll(){
        log.info("GET user");
        return userStorage.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userStorage.getUser(id);
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {
        userStorage.addUser(user);
        log.info("Пользователь успешно добавлен " + user);
        return user;
    }

    @PutMapping("/users")
    public User put(@Valid @RequestBody User user) {
        if (userStorage.containsKey(user.getId())) {
            userStorage.updateUser(user);
            log.info("Пользователь успешно обновлён " + user);
            return user; //изменение
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        System.out.println(userStorage.getAllUsers());
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable Integer friendId)  {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id)  {
        return userService.getFriendsList(id);
    }

    @GetMapping("/users/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int friendId)  {
        return userService.getCommonFriendsList(id, friendId);
    }
}
