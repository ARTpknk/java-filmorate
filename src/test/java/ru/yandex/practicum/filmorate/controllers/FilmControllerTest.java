package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTest {
    LocalDate titanikRelease = LocalDate.of(1997, 12, 19);
    LocalDate fakeRelease = LocalDate.of(97, 12, 19);
    Film titanik = new Film("Титаник", "Он утонул", titanikRelease, 194);
    Film titanik2 = new Film("", "Он утонул. Снова", titanikRelease, 194);
    Film titanik3 = new Film("Титаник3", "Он тонул и тонул и тонул и тонул и тонул и тонул и тонул и " +
            "тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и " +
            "тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и " +
            "тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и " +
            "тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и тонул и " +
            "тонул и тонул и тонул и тонул и тонул и ", titanikRelease, 194);
    Film titanik4 = new Film("Титаник", "Он утонул. Снова", fakeRelease, 194);
    Film titanik5 = new Film("Титаник", "Он утонул", titanikRelease, -194);
    FilmController filmController = new FilmController();

    @Test
    public void test() {
        filmController.create(titanik);
        titanik.setId(1);
        assertTrue(filmController.findAll().contains(titanik));

        filmController.create(titanik2);
        titanik2.setId(2);
        assertFalse(filmController.findAll().contains(titanik2));

        filmController.create(titanik2);
        titanik2.setId(2);
        assertFalse(filmController.findAll().contains(titanik2));

        filmController.create(titanik3);
        titanik3.setId(2);
        assertFalse(filmController.findAll().contains(titanik3));

        filmController.create(titanik4);
        titanik4.setId(2);
        assertFalse(filmController.findAll().contains(titanik4));

        filmController.create(titanik5);
        titanik5.setId(2);
        assertFalse(filmController.findAll().contains(titanik5));
    }


}
