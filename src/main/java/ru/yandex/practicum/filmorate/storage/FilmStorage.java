package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.Collection;

public interface FilmStorage {
    void addFilm(Film film);

    void removeFilm(Film film);

    void updateFilm(Film film);

    Collection<Film> getAllFilms();

    Film getFilm(int id);

    void updateFilmWithLikes(Film film);

}
