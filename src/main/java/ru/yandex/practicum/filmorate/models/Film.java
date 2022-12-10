package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class Film {
    protected final int id;
    protected final String name;
    @NonNull
    protected String description;
    @NonNull
    protected LocalDate releaseDate;
    @NonNull
    protected int duration;
}
