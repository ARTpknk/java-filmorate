package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;
import java.time.LocalDate;

@Data
public class User {
    protected final int id;
    @NonNull
    protected String email;
    protected final String login;
    @NonNull
    protected String name;
    @NonNull
    protected LocalDate  birthday;
}
