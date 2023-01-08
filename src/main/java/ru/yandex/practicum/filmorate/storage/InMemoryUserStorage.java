package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    protected int id = 0;

    @Override
    public void addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        Set<Long> friends = new HashSet<>();
        user.setFriends(friends);
        users.put(id, user);

    }

    @Override
    public void removeUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
        } else {
            throw new ValidationException("такого пользователя не существует");
        }
    }

    @Override
    public void updateUser(User user) {
        user.setFriends(users.get(user.getId()).getFriends());
        users.put(user.getId(), user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    public boolean containsKey(int id) {
        return users.containsKey(id);
    } //не знаю, как сделать private, ведь он используется в сервисе

    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(" ");
        }
        return users.get(id);
    }
}
