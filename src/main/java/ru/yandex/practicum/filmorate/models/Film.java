package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@Data
public class Film {
    protected int id;
    @NonNull
    @NotBlank
    @NotEmpty
    protected final String name;
    @NonNull
    @Max(280)
    protected String description;
    protected final LocalDate releaseDate;
    @Positive
    protected final int duration;
}
