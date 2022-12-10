package ru.yandex.practicum.filmorate.controllers;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTest {
    LocalDate birthday = LocalDate.of(1997, 12, 19);
    LocalDate fakebirthday = LocalDate.of(19973, 12, 19);

    User igor = new User("igor@gmail.com", "igoriok",  birthday);
    User igor2 = new User("igorgmail.com", "igoriok", birthday);
    User igor3 = new User("igor@gmail.com", "igor i ok",  birthday);
    User igor4 = new User("igor@gmail.com", "igoriok", fakebirthday);
    User igor5 = new User("igor@gmail.com", "igoriok",  birthday);

    UserController userController = new UserController();


    @Test
    public void test() {
        userController.create(igor);
        igor.setId(1);
        assertTrue(userController.findAll().contains(igor));

        userController.create(igor2);
        igor2.setId(2);
        assertFalse(userController.findAll().contains(igor2));

        userController.create(igor3);
        igor3.setId(2);
        assertFalse(userController.findAll().contains(igor3));

        userController.create(igor4);
        igor4.setId(2);
        assertFalse(userController.findAll().contains(igor4));

        userController.create(igor5);
        igor5.setId(2);
        igor5.setName(igor5.getLogin());
        assertTrue(userController.findAll().contains(igor5));
    }
}
