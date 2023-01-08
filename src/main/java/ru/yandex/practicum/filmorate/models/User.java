package ru.yandex.practicum.filmorate.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class User {
    private int id;
    private Set<Long> friends;
    @NonNull
    @Email
    private String email;
    @NonNull
    @NotBlank
    @NotEmpty
    private String login;
    private String name;
    @NonNull
    @PastOrPresent
    private LocalDate birthday;

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}
