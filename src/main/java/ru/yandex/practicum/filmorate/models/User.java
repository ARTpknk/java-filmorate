package ru.yandex.practicum.filmorate.models;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    protected int id;
    protected Set<Long> friends;
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

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}
