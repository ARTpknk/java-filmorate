package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    protected int id;
    @NonNull
    @Email
    protected String email;
    @NonNull
    @NotBlank
    @NotEmpty
    protected String login;
    protected String name;
    @NonNull
    @PastOrPresent
    protected LocalDate birthday;
}
