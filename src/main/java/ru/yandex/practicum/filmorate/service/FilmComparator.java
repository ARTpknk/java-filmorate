package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.Comparator;

public class FilmComparator implements Comparator<Film> {

    public int compare(Film o1, Film o2) {
        return o2.getLikes().size() - o1.getLikes().size();
    }

}
