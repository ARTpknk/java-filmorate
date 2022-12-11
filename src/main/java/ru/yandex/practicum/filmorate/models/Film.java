package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    protected int id;
    @NonNull
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
}



