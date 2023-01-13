package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

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

        String sqlQuery = "insert into USERS(USER_NAME, USER_EMAIL, USER_LOGIN, USER_BIRTHDAY) " +
                "values ( ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());

    }

    public void findException(User user) {
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
        String sqlUpdate = "select USER_ID FROM USERS";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(user.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            String sqlQuery = "delete from USERS where id = ?";
            jdbcTemplate.update(sqlQuery, user.getId());
        }
    }

    @Override
    public void updateUser(User user) {
        checkIsUserExists(user);
        String sqlQuery = "update USERS set USER_NAME=?, USER_EMAIL=?, USER_LOGIN=?, USER_BIRTHDAY=? " +
                "where USER_ID=?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
    }

    public void checkIsUserExists(User user) {
        findException(user);
        String sqlUpdate = "select USER_ID from USERS";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(user.getId())) {
            throw new NotFoundException("id не найден");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS");
        while (userRows.next()) {
            users.add(getUserMethod(userRows));
        }
        return users;
    }


    @Override
    public User getUser(int id) {
        String sqlUpdate = "select USER_ID from USERS";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(id)) {
            throw new NotFoundException("id не найден");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID =? ", id);
        if (userRows.next()) {
            return getUserMethod(userRows);
        } else {
            return null;
        }
    }

    public User getUserMethod(SqlRowSet userRows) {
        User user = new User(Objects.requireNonNull(userRows.getString("USER_EMAIL")),
                Objects.requireNonNull(userRows.getString("USER_LOGIN")),
                Objects.requireNonNull(userRows.getDate("USER_BIRTHDAY")).toLocalDate());
        user.setId(userRows.getInt("USER_ID"));
        user.setName(userRows.getString("USER_NAME"));

        String sqlUpdate = ("select SECOND_FRIEND_ID from FRIENDSHIP where FIRST_FRIEND_ID = " + user.getId());
        Set<Long> idSet = new HashSet<>(jdbcTemplate.queryForList(sqlUpdate, Long.class));
        String sqlUpdate2 = ("select SECOND_FRIEND_ID from FRIENDSHIP where SECOND_FRIEND_ID = " + user.getId());
        Set<Long> idSet2 = new HashSet<>(jdbcTemplate.queryForList(sqlUpdate2, Long.class));
        idSet.addAll(idSet2);
        user.setFriends(idSet);
        return user;
    }

    public void addFriend(int id1, int id2) {
        if (getUser(id1) == null) {
            throw new NotFoundException(" ");
        }
        if (getUser(id2) == null) {
            throw new NotFoundException(" ");
        }
        String sqlQuery = "insert into FRIENDSHIP (FIRST_FRIEND_ID, SECOND_FRIEND_ID)" +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    public void deleteFriend(int id1, int id2) {
            String sqlQuery = "delete from FRIENDSHIP where FIRST_FRIEND_ID = ? and SECOND_FRIEND_ID = ? ";
            jdbcTemplate.update(sqlQuery, id1, id2);
    }

    public List<User> getFriendsList(int id) {
        List<User> friends = new ArrayList<>();
        String sqlUpdate = ("select SECOND_FRIEND_ID from FRIENDSHIP where FIRST_FRIEND_ID = " + id);
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        for (Integer id4 : idList) {
            friends.add(getUser(id4));
        }
        return friends;
    }


    public List<User> getCommonFriendsList(int id1, int id2) {






        return getFriendsList(id1).stream().filter(s -> getFriendsList(id2).contains(s)).collect(Collectors.toList());
    }


    @Override
    public boolean containsKey(int id) {
        return false;
    }
}
