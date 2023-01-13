package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

import java.util.Collection;

@Component
public class UserDbStorage implements UserStorage {
    protected int id = 0;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) {
        findException(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);

        String sqlQuery = "insert into USERS(USER_NAME, USER_EMAIL, USER_LOGIN, USER_BIRTHDAY) " +
                "values ( ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());

    }

    public void findException(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("Нет почты");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("Нет почты");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Вы родились завтра?");
        }
    }

    @Override
    public void removeUser(User user) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public boolean containsKey(int id) {
        return false;
    }

    @Override
    public User getUser(int id) {
        return null;
    }
}
