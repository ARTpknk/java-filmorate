package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    protected int id = 0;
    LocalDate now = LocalDate.now();

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET user");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Отсутствие имени, заменено логином " + user.getLogin());
            id++;
            user.setId(id);
            users.put(id, user);
            return user;//ошибка 5
        }
        id++;
        user.setId(id);
        users.put(id, user);
        log.info("Пользователь успешно добавлен " + user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) { //изменено
            if (user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.info("Отсутствие имени, заменено логином " + user.getLogin());
                users.put(user.getId(), user);//изменение
                return user;
            }
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлён " + user);
            return user; //изменение
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
