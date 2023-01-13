package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    protected int id = 0;
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    LocalDate firstMovie = LocalDate.of(1895, 12, 28);

    @Override
    public void addFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.info("Слишком длинное описание " + film.getDescription());
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(firstMovie)) {
            log.info("Неверная дата " + film.getReleaseDate());
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
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
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.info("Слишком длинное описание " + film.getDescription());
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(firstMovie)) {
            log.info("Неверная дата " + film.getReleaseDate());
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        Set<Integer> likes = new HashSet<>();
        film.setLikes(likes);
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Такого фильма не существует");
        }
        return films.get(id);
    }

    @Override
    public void updateFilmWithLikes(int id, int userId) {
        Film film = getFilm(id);
        if (!film.getLikes().contains(userId)) {
            Set<Integer> buffer = film.getLikes();
            buffer.add(userId);
            film.setLikes(buffer);
            films.put(id, films.get(id));
        } else {
            throw new ValidationException("пользователь уже поставил лайк");
        }
    }

    @Override
    public void deleteFilmWithLikes(int id, int userId) {
        Film film = getFilm(id);
        if (film.getLikes().contains(userId)) {
            Set<Integer> buffer = film.getLikes();
            buffer.remove(userId);
            film.setLikes(buffer);
            films.put(id, films.get(id));
        } else {
            throw new ValidationException("пользователь не ставил лайк");
        }
    }
}
