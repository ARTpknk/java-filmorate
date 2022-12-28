package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    LocalDate firstMovie = LocalDate.of(1895, 12, 28);
    protected int id = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET films");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            log.info("Слишком длинное описание " + film.getDescription());
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(firstMovie)) {
            log.info("Неверная дата " + film.getReleaseDate());
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        id++;
        film.setId(id);
        films.put(id, film);
        log.info("Фильм успешно добавлен " + film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (film.getDescription().length() > 200) {
            log.info("Слишком длинное описание " + film.getDescription());
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(firstMovie)) {
            log.info("Неверная дата " + film.getReleaseDate());
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлён " + film);
        return film;
    }
}
