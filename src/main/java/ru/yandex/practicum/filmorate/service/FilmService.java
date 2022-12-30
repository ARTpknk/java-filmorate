package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    protected FilmStorage filmStorage;
    protected UserStorage userStorage;
    FilmComparator filmComparator = new FilmComparator();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int id, int userId) {
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
        return filmStorage.getAllFilms().stream()
                .sorted(filmComparator)
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }


}
