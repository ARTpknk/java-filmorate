package ru.yandex.practicum.filmorate.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Film implements Comparable<Film> {
    int id;
    Set<Integer> likes;
    @NotBlank
    String name;
    @NonNull
    String description;
    @NonNull
    LocalDate releaseDate;
    @Positive
    @NonNull
    int duration;
    Genre[] genres;
    Mpa mpa;

    public Film(String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

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