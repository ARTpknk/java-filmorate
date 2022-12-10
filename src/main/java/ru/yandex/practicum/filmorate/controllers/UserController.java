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
        try {
            if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.info("Email заполнен неверно " + user.getEmail());

                //httpExchange.sendResponseHeaders(200, 0);
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND);

                throw new ValidationException("Email заполнен неверно");
            }
            if (user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                log.info("Логин заполнен неверно " + user.getLogin());
                throw new ValidationException("Логин заполнен неверно");
            }
            if (user.getBirthday().isAfter(now)) {  //ошибка 1
                log.info("День рождения заполнен неверно " + user.getBirthday());
                throw new ValidationException("День рождения заполнен неверно");
            }
            if (user.getName().isEmpty() || user.getName().isBlank()) {
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
//??????
        }catch (RuntimeException ignored){

        }
        return user;
    }

    @PutMapping
    public User put(@Valid  @RequestBody User user) throws ValidationException {
        if(users.containsKey(user.getId())){ //изменено
            if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.info("Email заполнен неверно " + user.getEmail());
                throw new ValidationException("Email заполнен неверно");
            }
            if(user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")){
                log.info("Логин заполнен неверно " + user.getLogin());
                throw new ValidationException("Логин заполнен неверно");
            }
            if(user.getBirthday().isAfter(LocalDate.now())){ //возможная ошибка
                log.info("День рождения заполнен неверно " + user.getBirthday());
                throw new ValidationException("День рождения заполнен неверно");
            }
            if(user.getName().isEmpty() || user.getName().isBlank()){
                user.setName(user.getLogin());
                log.info("Отсутствие имени, заменено логином " + user.getLogin());
                users.put(user.getId(), user);//изменение
                return user;
            }
            users.put(user.getId(), user);
            log.info("Пользователь успешно обновлён " + user);
            return user; //изменение
        }
        else{
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
