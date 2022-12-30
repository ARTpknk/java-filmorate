package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    protected FilmStorage filmStorage;
    protected UserStorage userStorage;
    protected FilmComparator filmComparator;
    LocalDate firstMovie = LocalDate.of(1895, 12, 28);


    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, FilmComparator filmComparator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmComparator = filmComparator;
    }

    public void addLike(int id, int userId) {
        if (userId < 1) {
            throw new ValidationException(" ");
        }
        Film film = filmStorage.getFilm(id);
        if (!film.getLikes().contains(userId)) {
            Set<Integer> buffer = film.getLikes();
            buffer.add(userId);
            film.setLikes(buffer);
            filmStorage.updateFilmWithLikes(film);
        } else {
            throw new ValidationException("пользователь уже поставил лайк");
        }
    }

    public void deleteLike(int id, int userId) {
        if (userId < 1) {
            throw new NotFoundException(" ");
        }
        if (!userStorage.containsKey(userId)) {
            throw new NotFoundException(" ");
        } else {
            Film film = filmStorage.getFilm(id);
            if (film.getLikes().contains(userId)) {
                Set<Integer> buffer = film.getLikes();
                buffer.remove(userId);
                film.setLikes(buffer);
                filmStorage.updateFilmWithLikes(film);
            } else {
                throw new ValidationException("пользователь не ставил лайк");
            }
        }
    }

    public List<Film> getPopularFilms(int count) {
        if (filmStorage.getAllFilms().isEmpty()) {
            throw new NotFoundException("Фильмы не созданы");
        }
        return filmStorage.getAllFilms().stream()
                .sorted(filmComparator)
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> findAll() {
        return filmStorage.getAllFilms();
    }

    public Film findFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public void updateFilm(Film film) {
        if (!filmStorage.containsKey(film)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (filmStorage.checkDescriptionLength(film)) {
            log.info("Слишком длинное описание " + film.getDescription());
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(firstMovie)) {
            log.info("Неверная дата " + film.getReleaseDate());
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        filmStorage.updateFilm(film);
        log.info("Фильм успешно обновлён ");
    }

    public void addFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.info("Слишком длинное описание " + film.getDescription());
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(firstMovie)) {
            log.info("Неверная дата " + film.getReleaseDate());
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        filmStorage.addFilm(film);
    }

}
