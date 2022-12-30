package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    protected UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer id1, Integer id2) {
        if (userStorage.getUser(id1) == null) {
            throw new NotFoundException(" ");
        }
        if (userStorage.getUser(id2) == null) {
            throw new NotFoundException(" ");
        } else {
            User user1 = userStorage.getUser(id1);
            User user2 = userStorage.getUser(id2);
            System.out.println(user1);
            System.out.println(user2);
            user1.addFriend((long) id2);
            user2.addFriend((long) id1);
            userStorage.updateUser(user1);
            userStorage.updateUser(user2);
        }
    }

    public void deleteFriend(int id1, int id2) {
        userStorage.getUser(id1).deleteFriend((long) id2);
        userStorage.getUser(id2).deleteFriend((long) id1);
        userStorage.updateUser(userStorage.getUser(id1));
        userStorage.updateUser(userStorage.getUser(id2));
    }

    public List<User> getFriendsList(int id) {
        List<User> friends = new ArrayList<>();
        for (Long id1 : userStorage.getUser(id).getFriends()) {
            friends.add(userStorage.getUser(Math.toIntExact(id1)));
        }
        return friends;
    }

    public List<User> getCommonFriendsList(int id1, int id2) {
        List<User> commonFriends = new ArrayList<>();
        for (Long id :
                userStorage.getUser(id1).getFriends().stream().filter(s -> userStorage.getUser(id2).getFriends().contains(s)).collect(Collectors.toList())) {
            commonFriends.add(userStorage.getUser(Math.toIntExact(id)));
        }
        return commonFriends;
    }
}
