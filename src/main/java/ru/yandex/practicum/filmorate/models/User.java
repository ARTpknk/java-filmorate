package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class User {
    protected int id;
    @Email
    protected final String email;
    @NonNull
    @NotBlank
    @NotEmpty
    protected String login;
    @NonNull
    protected String name;
    protected final LocalDate  birthday;
}
