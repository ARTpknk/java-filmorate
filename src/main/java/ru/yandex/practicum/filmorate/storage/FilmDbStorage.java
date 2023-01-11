package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements FilmStorage {

    public static final int MAX_DESCRIPTION_LENGTH = 200;
    LocalDate firstMovie = LocalDate.of(1895, 12, 28);
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    protected int id = 0;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) {
        findException(film);
        id++;
        film.setId(id);

        if(film.getMpa() != null){
            String[] mpaSplit = film.getMpa().toString().split("=");
            String[] mpaSplit2 = mpaSplit[1].split("}");
            String sqlQuery = "insert into FILM(MPA_ID, FILM_RELEASE_DATE, FILM_DURATION, FILM_NAME, FILM_DESCRIPTION) " +
                    "values ( ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery, mpaSplit2[0], film.getReleaseDate(), film.getDuration(), film.getName(),
                    film.getDescription());
        }
        else{
            String sqlQuery = "insert into FILM(FILM_RELEASE_DATE, FILM_DURATION, FILM_NAME, FILM_DESCRIPTION) " +
                    "values (?, ?, ?, ?)";
            jdbcTemplate.update(sqlQuery, film.getReleaseDate(), film.getDuration(), film.getName(),
                    film.getDescription());
        }

       if(film.getGenres() != null){
           if(film.getGenres().length>1){
               film.setGenres(makeGenresUnique(film));
           }
               ArrayList<Object> listGenres = new ArrayList<>();
               Collections.addAll(listGenres, film.getGenres());
               ArrayList<String> readyGenres = new ArrayList<>();
               for(int i=0; i<listGenres.size(); i++){
                   String[] g =  listGenres.get(i).toString().split("=");
                   String[] g2 = g[1].split("}");
                   readyGenres.add(i, g2[0]);
               }
               for(String g: readyGenres){
                   String sqlQuery = "insert into GENRE_FILM(GENRE_ID, FILM_ID) " + "values (?,?)";
                   jdbcTemplate.update(sqlQuery, g, film.getId());
               }
        }
    }
    @Override
    public void updateFilm(Film film) {
        findException(film);
        String sqlUpdate = "select FILM_ID from FILM";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(film.getId())) {
            throw new NotFoundException("id не найден");
        }

        if(film.getMpa() != null){
            String[] mpaSplit = film.getMpa().toString().split("=");
            String[] mpaSplit2 = mpaSplit[1].split("}");
            String sqlQuery = "update FILM set MPA_ID=?, FILM_RELEASE_DATE=?, FILM_DURATION=?, FILM_NAME=?, FILM_DESCRIPTION=? where FILM_ID=? ";
            jdbcTemplate.update(sqlQuery, mpaSplit2[0], film.getReleaseDate(), film.getDuration(), film.getName(),
                    film.getDescription(), film.getId());
        }
        else{
            String sqlQuery = "update FILM set FILM_RELEASE_DATE=?, FILM_DURATION=?, FILM_NAME=?, FILM_DESCRIPTION=? where FILM_ID=? ";
            jdbcTemplate.update(sqlQuery, film.getReleaseDate(), film.getDuration(), film.getName(),
                    film.getDescription(), film.getId());
        }

        if(film.getGenres() != null){
            if(film.getGenres().length>1){
                film.setGenres(makeGenresUnique(film));
            }
            String sqlQuery = "delete from GENRE_FILM where exists(select FILM_ID from GENRE_FILM where FILM_ID=?)";
            jdbcTemplate.update(sqlQuery, film.getId());
            Set<Object> setGenres = new HashSet<>();
            Collections.addAll(setGenres, film.getGenres());
            Set<String> readyGenres = new HashSet<>();
            for (Object listGenre : setGenres) {
                String[] g = listGenre.toString().split("=");
                String[] g2 = g[1].split("}");
                readyGenres.add(g2[0]);
            }
            for(String g: readyGenres){
                String sqlQuery2 = "insert into GENRE_FILM(GENRE_ID, FILM_ID) " + "values (?,?)";
                jdbcTemplate.update(sqlQuery2, g, film.getId());
            }
        }
    }
    public void findException(Film film){
        if(film.getName().isEmpty()||film.getName().isBlank()){
            throw new ValidationException("Нет имени");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.info("Слишком длинное описание " + film.getDescription());
            throw new ValidationException("Слишком длинное описание");
        }
        if (film.getReleaseDate().isBefore(firstMovie)) {
            log.info("Неверная дата " + film.getReleaseDate());
            throw new ValidationException("Неверная дата, тогда ещё не снимали фильмы");
        }
        if(film.getDuration()<1){
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
    }

    public Object[] makeGenresUnique (Film film){
        ArrayList<Object> listGenres = new ArrayList<>();
        Collections.addAll(listGenres, film.getGenres());
        ArrayList<Object> uniqueGenres = (ArrayList<Object>) listGenres.stream().distinct().collect(Collectors.toList());
        Object[]finalGenres = new Object[uniqueGenres.size()];
        for(int i=0; i<uniqueGenres.size(); i++){
            finalGenres[i]=uniqueGenres.get(i);
        }
        return finalGenres;
    }

    @Override
    public void removeFilm(Film film) {
        String sqlUpdate;
        sqlUpdate = "select FILM_ID from FILM";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(film.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            String sqlQuery = "delete from FILM where id = ?";
            jdbcTemplate.update(sqlQuery, film.getId());
        }
    }






    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film getFilm(int id) {
        /*
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where film_id = ?", id);
        if(filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("FILM_ID"),
                    filmRows.getString("FILM_NAME"),
                    Objects.requireNonNull(filmRows.getString("FILM_DESCRIPTION")),
            Objects.requireNonNull(filmRows.getDate("FILM_RELEASE_DATE")).toLocalDate(),
            filmRows.getInt("FILM_DURATION"));
            Integer i = filmRows.getInt("MPA_ID");
            String i2 = i.toString();
            String mpa = ("id=" + i2);
            film.setMpa(mpa);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return null;
        }

         */
        return null;
    }





    @Override
    public void updateFilmWithLikes(Film film) {

    }


}


