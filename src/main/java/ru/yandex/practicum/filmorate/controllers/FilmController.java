package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<String, Film> films = new HashMap<>();
    LocalDate firstMovie = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> findAll() {
        // log.trace();
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if(film.getName() == null || film.getName().isBlank()){
            log.info("Пустое название фильма");
            throw new ValidationException("Заполните название фильма");
        }
        if(film.getDescription().length()>280){
            log.info("Слишком длинное описание");
            throw new ValidationException("Слишком длинное описание");
        }
        if(film.getReleaseDate().isBefore(firstMovie) ){
            log.info("Неверная дата");
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        if(film.getDuration()<0){
            log.info("Неверная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        films.put(film.getName(), film);
        log.info("Фильм успешно добавлен");
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        if(film.getName() == null || film.getName().isBlank()){
            log.info("Пустое название фильма");
            throw new ValidationException("Заполните название фильма");
        }
        if(film.getDescription().length()>280){
            log.info("Слишком длинное описание");
            throw new ValidationException("Слишком длинное описание");
        }
        if(film.getReleaseDate().isBefore(firstMovie) ){
            log.info("Неверная дата");
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        if(film.getDuration()<0){
            log.info("Неверная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        films.put(film.getName(), film);
        log.info("Фильм успешно обновлён");
        return film;
    }
}
