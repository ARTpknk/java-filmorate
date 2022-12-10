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
    protected String name;
    @NonNull
    @Max(200) //???????????
    protected String description;
    @NonNull
    protected LocalDate releaseDate;
    @Positive//???????
    @NonNull
    protected int duration;
}



