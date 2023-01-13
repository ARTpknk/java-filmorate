package ru.yandex.practicum.filmorate.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor

public class Film implements Comparable<Film> {
    private int id;
    private Set<Integer> likes;
    @NotBlank
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Positive
    @NonNull
    private int duration;
    private Genre[] genres;
    private Mpa mpa;

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", likes=" + likes +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", genres=" + Arrays.toString(genres) +
                ", mpa=" + mpa +
                '}';
    }

    @Override
    public int compareTo(Film film) {
        return this.likes.size() - film.likes.size();
    }
}



