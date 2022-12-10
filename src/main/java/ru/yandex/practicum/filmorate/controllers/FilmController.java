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

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<String, Film> films = new HashMap<>();
    LocalDate firstMovie = LocalDate.of(1895, 12, 28);
    protected int id = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("GET films");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        try {
            if (film.getName().isBlank() || film.getName().isEmpty()) {
                log.info("Пустое название фильма " + film);
                throw new ValidationException("Заполните название фильма");
            }
            if (film.getDescription().length() > 280) {
                log.info("Слишком длинное описание " + film.getDescription());
                throw new ValidationException("Слишком длинное описание");
            }
            if (film.getReleaseDate().isBefore(firstMovie)) {
                log.info("Неверная дата " + film.getReleaseDate());
                throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
            }
            if (film.getDuration() < 0) {
                log.info("Неверная продолжительность фильма " + film.getDuration());
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            } else {
                id++;
                film.setId(id);
                films.put(film.getName(), film);
                log.info("Фильм успешно добавлен " + film);
            }
        }catch(RuntimeException ignored){

        }

        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if(films.containsKey(film.getName())){
            films.put(film.getName(), film);
            log.info("Фильм успешно обновлён " + film);
        }
        else{
            if(film.getName().isBlank()){
                log.info("Пустое название фильма " + film);
                throw new ValidationException("Заполните название фильма");
            }
            if(film.getDescription().length()>280){
                log.info("Слишком длинное описание " + film.getDescription());
                throw new ValidationException("Слишком длинное описание");
            }
            if(film.getReleaseDate().isBefore(firstMovie) ){
                log.info("Неверная дата " + film.getReleaseDate());
                throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
            }
            if(film.getDuration()<0){
                log.info("Неверная продолжительность фильма " + film.getDuration());
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            }
            id++;
            film.setId(id);
            films.put(film.getName(), film);
            log.info("Фильм успешно добавлен " + film);
        }
        return film;
    }
}
