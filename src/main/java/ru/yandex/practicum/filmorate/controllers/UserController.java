package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<String, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if(user.getEmail().isEmpty() || user.getEmail().isBlank() || user.getEmail().contains("@")){
            log.info("Email заполнен неверно");
            throw new ValidationException("Email заполнен неверно");
        }
        if(user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            log.info("Логин заполнен неверно");
            throw new ValidationException("Логин заполнен неверно");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            log.info("День рождения заполнен неверно");
            throw new ValidationException("День рождения заполнен неверно");
        }
        if(user.getName().isEmpty() || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        users.put(user.getEmail(), user);
        log.info("Пользователь успешно добавлен");
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if(user.getEmail().isEmpty() || user.getEmail().isBlank() || user.getEmail().contains("@")){
            log.info("Email заполнен неверно");
            throw new ValidationException("Email заполнен неверно");
        }
        if(user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            log.info("Логин заполнен неверно");
            throw new ValidationException("Логин заполнен неверно");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            log.info("День рождения заполнен неверно");
            throw new ValidationException("День рождения заполнен неверно");
        }
        if(user.getName().isEmpty() || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        users.put(user.getEmail(), user);
        log.info("Пользователь успешно обновлён");
        return user;
    }
}
