package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Qualifier("filmDbStorage")
@Slf4j
@Service
public class FilmService {
    protected FilmDbStorage filmStorage;
    protected UserDbStorage userStorage;
    protected FilmComparator filmComparator;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, FilmComparator filmComparator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmComparator = filmComparator;
    }

    public void addLike(int id, int userId) {
        if (userId < 1) {
            throw new ValidationException(" ");
        }
        filmStorage.updateFilmWithLikes(id, userId);
    }

    public void deleteLike(int id, int userId) {
        if (userId < 1) {
            throw new NotFoundException("Пользователь с отрицательным id");
        } else {
            filmStorage.deleteFilmWithLikes(id, userId);
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
        filmStorage.updateFilm(film);
        log.info("Фильм успешно обновлён ");
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public Genre[] getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenre(int id) {
        return filmStorage.getGenre(id);
    }

    public Mpa[] getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Mpa getMpa(int id) {
        return filmStorage.getMpa(id);
    }
}
