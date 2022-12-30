package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    protected int id = 0;
    public static final int MAX_DESCRIPTION_LENGTH = 200;

    @Override
    public void addFilm(Film film) {
        Set<Integer> likes = new HashSet<>();
        film.setLikes(likes);
        id++;
        film.setId(id);
        films.put(id, film);
    }

    @Override
    public void removeFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
        } else {
            throw new ValidationException("такого фильма не существует");
        }
    }

    @Override
    public void updateFilm(Film film) {
        Set<Integer> likes = new HashSet<>();
        film.setLikes(likes);
        films.put(film.getId(), film);
    }


    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public boolean containsKey(Film film) {
        return films.containsKey(film.getId());
    } //не знаю, как сделать private, ведь он используется в сервисе

    public boolean checkDescriptionLength(Film film) {
        return film.getDescription().length() > MAX_DESCRIPTION_LENGTH;
    }

    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Такого фильма не существует");
        }
        return films.get(id);
    }

    @Override
    public void updateFilmWithLikes(Film film) {
        films.put(film.getId(), film);
    }
}
