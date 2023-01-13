package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    protected UserDbStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage) {
        this.userStorage= userStorage;
    }

    public void addFriend(Integer id1, Integer id2) {
        userStorage.addFriend(id1, id2);
    }

    public void deleteUser(int id){
        userStorage.removeUser(userStorage.getUser(id));
    }

    public void deleteFriend(int id1, int id2) {
        userStorage.deleteFriend(id1, id2);
    }

    public List<User> getFriendsList(int id) {
        return userStorage.getFriendsList(id);
    }

    public List<User> getCommonFriendsList(int id1, int id2) {
        return userStorage.getCommonFriendsList(id1, id2);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public void addUser(User user) {
        userStorage.addUser(user);
    }

    public void put(User user) {
        userStorage.updateUser(user);
    }
}
