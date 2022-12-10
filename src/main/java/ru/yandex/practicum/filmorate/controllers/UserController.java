package ru.yandex.practicum.filmorate.controllers;

import com.sun.net.httpserver.HttpExchange;
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
    private final HashMap<String, User> users = new HashMap<>();
    protected int id = 0;

    @GetMapping
    public Collection<User> findAll() {
        log.info("GET user");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if(user.getEmail().isEmpty() || user.getEmail().isBlank() || user.getEmail().contains("@")){
            log.info("Email заполнен неверно " + user.getEmail());

            //httpExchange.sendResponseHeaders(200, 0);
           // throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            throw new ValidationException("Email заполнен неверно");
        }
        if(user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            log.info("Логин заполнен неверно " + user.getLogin());
            throw new ValidationException("Логин заполнен неверно");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            log.info("День рождения заполнен неверно " + user.getBirthday());
            throw new ValidationException("День рождения заполнен неверно");
        }
        if(user.getName().isEmpty() || user.getName().isBlank()){
            user.setName(user.getLogin());
            log.info("Отсутствие имени, заменено логином " + user.getLogin());
        }
        id++;
        user.setId(id);
        users.put(user.getEmail(), user);
        log.info("Пользователь успешно добавлен " + user);
        return user;
    }

    @PutMapping
    public User put(@Valid  @RequestBody User user) {
        if(users.containsKey(user.getName())){
            users.put(user.getName(), user);
            log.info("Пользователь успешно обновлён " + user);
        }
        else{
            if(user.getEmail().isEmpty() || user.getEmail().isBlank() || user.getEmail().contains("@")){
                log.info("Email заполнен неверно " + user.getEmail());
                throw new ValidationException("Email заполнен неверно");
            }
            if(user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")){
                log.info("Логин заполнен неверно " + user.getLogin());
                throw new ValidationException("Логин заполнен неверно");
            }
            if(user.getBirthday().isAfter(LocalDate.now())){
                log.info("День рождения заполнен неверно " + user.getBirthday());
                throw new ValidationException("День рождения заполнен неверно");
            }
            if(user.getName().isEmpty() || user.getName().isBlank()){
                user.setName(user.getLogin());
                log.info("Отсутствие имени, заменено логином " + user.getLogin());
            }
            id++;
            user.setId(id);
            users.put(user.getEmail(), user);
        }

        log.info("Пользователь успешно добавлен " + user);
        return user;
    }
}
