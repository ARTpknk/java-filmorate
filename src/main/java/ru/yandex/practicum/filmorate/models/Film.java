package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class Film {
    protected int id;
    @NonNull
    @NotBlank
    @NotEmpty
    protected final String name;
    @NonNull
    protected String description;
    protected final LocalDate releaseDate;
    protected final int duration;
}
