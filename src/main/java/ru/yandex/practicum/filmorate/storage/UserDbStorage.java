package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

        String sqlQuery = "INSERT INTO users(user_name, user_email, user_login, user_birthday) " +
                "VALUES ( ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
    }

    @Override
    public User getUser(int id) {
        String sqlUpdate = "SELECT user_id FROM users";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(id)) {
            throw new NotFoundException("id не найден");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id =? ", id);
        if (userRows.next()) {
            return getUserMethod(userRows);
        } else {
            return null;
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (userRows.next()) {
            users.add(getUserMethod(userRows));
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        checkIsUserExists(user);
        String sqlQuery = "UPDATE users SET user_name=?, user_email=?, user_login=?, user_birthday=? " +
                "WHERE user_id=?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
    }

    public void addFriend(int id1, int id2) {
        if (getUser(id1) == null) {
            throw new NotFoundException(" ");
        }
        if (getUser(id2) == null) {
            throw new NotFoundException(" ");
        }
        String sqlQuery = "INSERT INTO friendship (first_friend_id, second_friend_id)" +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    public void deleteFriend(int id1, int id2) {
        String sqlQuery = "DELETE FROM friendship WHERE first_friend_id = ? AND second_friend_id = ? ";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    public List<User> getFriendsList(int id) {
        List<User> friends = new ArrayList<>();
        String sqlUpdate = ("SELECT second_friend_id FROM friendship WHERE first_friend_id = " + id);
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        for (Integer id4 : idList) {
            friends.add(getUser(id4));
        }
        return friends;
    }

    public List<User> getCommonFriendsList(int id1, int id2) {
        return getFriendsList(id1).stream().filter(s -> getFriendsList(id2).contains(s)).collect(Collectors.toList());
    }

    private void findException(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Нет почты");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("Нет логина");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Вы родились завтра?");
        }
    }

    @Override
    public void removeUser(User user) {
        String sqlUpdate = "SELECT user_id FROM users";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(user.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            String sqlQuery = "DELETE FROM users WHERE id = ?";
            jdbcTemplate.update(sqlQuery, user.getId());
        }
    }

    private void checkIsUserExists(User user) {
        findException(user);
        String sqlUpdate = "SELECT user_id FROM users";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(user.getId())) {
            throw new NotFoundException("id не найден");
        }
    }

    private User getUserMethod(SqlRowSet userRows) {
        User user = new User(Objects.requireNonNull(userRows.getString("user_email")),
                Objects.requireNonNull(userRows.getString("user_login")),
                Objects.requireNonNull(userRows.getDate("user_birthday")).toLocalDate());
        user.setId(userRows.getInt("user_id"));
        user.setName(userRows.getString("user_name"));

        String sqlUpdate = ("SELECT second_friend_id FROM friendship WHERE first_friend_id = " + user.getId());
        Set<Long> idSet = new HashSet<>(jdbcTemplate.queryForList(sqlUpdate, Long.class));
        String sqlUpdate2 = ("SELECT second_friend_id FROM friendship WHERE second_friend_id = " + user.getId());
        Set<Long> idSet2 = new HashSet<>(jdbcTemplate.queryForList(sqlUpdate2, Long.class));
        idSet.addAll(idSet2);
        user.setFriends(idSet);
        return user;
    }
}
