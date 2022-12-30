package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.Comparator;
@Component
public class FilmComparator implements Comparator<Film> {

    public int compare(Film o1, Film o2) {
        return o2.getLikes().size() - o1.getLikes().size();
    }

}
