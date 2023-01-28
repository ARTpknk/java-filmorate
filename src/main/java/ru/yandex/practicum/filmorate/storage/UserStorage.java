package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.models.User;

import java.util.Collection;

public interface UserStorage {
    void addUser(User user);

    void removeUser(User user);

    void updateUser(User user);

    Collection<User> getAllUsers();

    User getUser(int id);
}
