package ru.yandex.practicum.filmorate.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor

public class Film implements Comparable<Film> {
    private int id;
    private Set<Integer> likes;
    @NotBlank
    @NotNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Positive
    @NonNull
    private int duration;
    private Object[] genres;
    private Object mpa;



    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", likes=" + likes +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }

    @Override
    public int compareTo(Film film) {
        return this.likes.size() - film.likes.size();
    }

    public static int returnPopularity(Film film) {
        return film.getLikes().size();
    }



}



