package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
    protected int id;
    protected Set<Integer> likes;
    @NotBlank
    @NotEmpty
    protected String name;
    @NonNull
    protected String description;
    @NonNull
    protected LocalDate releaseDate;
    @Positive
    @NonNull
    protected int duration;

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



