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
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;

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
        String sqlQuery = "insert into FILM(MPA_ID, FILM_RELEASE_DATE, FILM_DURATION, FILM_NAME, FILM_DESCRIPTION) " +
                "values ( ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, film.getMpa().getId(), film.getReleaseDate(), film.getDuration(), film.getName(),
                film.getDescription());

        if (film.getGenres() != null) {
            if (film.getGenres().length > 1) {
                film.setGenres(makeGenresUnique(film));
            }
            for (Genre g : film.getGenres()) {
                String sqlQuery2 = "insert into GENRE_FILM(GENRE_ID, FILM_ID) " + "values (?,?)";
                jdbcTemplate.update(sqlQuery2, g.getId(), film.getId());
            }
        }
    }

    @Override
    public void updateFilm(Film film) {
        checkIsFilmExists(film);
        String sqlQuery = "update FILM set MPA_ID=?, FILM_RELEASE_DATE=?, FILM_DURATION=?, FILM_NAME=?, FILM_DESCRIPTION=? where FILM_ID=? ";
        jdbcTemplate.update(sqlQuery, film.getMpa().getId(), film.getReleaseDate(), film.getDuration(), film.getName(),
                film.getDescription(), film.getId());

        if (film.getGenres() != null) {
            if (film.getGenres().length > 1) {
                film.setGenres(makeGenresUnique(film));
            }

            String sqlQuery3 = "delete from GENRE_FILM where FILM_ID=?";
            jdbcTemplate.update(sqlQuery3, film.getId());

            for (Genre g : film.getGenres()) {
                String sqlQuery4 = "insert into GENRE_FILM(GENRE_ID, FILM_ID) " + "values (?,?)";
                jdbcTemplate.update(sqlQuery4, g.getId(), film.getId());
            }

        }
    }

    public void findException(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
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
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма отрицательная");
        }
    }

    public void checkIsFilmExists(Film film) {
        findException(film);
        String sqlUpdate = "select FILM_ID from FILM";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(film.getId())) {
            throw new NotFoundException("id не найден");
        }
    }

    public Genre[] makeGenresUnique(Film film) {
        ArrayList<Genre> listGenres = new ArrayList<>();
        Collections.addAll(listGenres, film.getGenres());
        ArrayList<Genre> uniqueGenres = (ArrayList<Genre>) listGenres.stream().distinct().collect(Collectors.toList());
        Genre[] finalGenres = new Genre[uniqueGenres.size()];
        for (int i = 0; i < uniqueGenres.size(); i++) {
            finalGenres[i] = uniqueGenres.get(i);
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
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM as f left join MPA AS m on f.MPA_ID = m.MPA_ID");
        while (filmRows.next()) {
            Film film = getFilmMethod(filmRows);
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE_FILM as gf left join GENRE AS g on gf.GENRE_ID = g.GENRE_ID where film_id = ?", id);
            List<Genre> genres = new ArrayList<>();
            int idList = 0;
            while (genreRows.next()) {
                int genreId = genreRows.getInt("GENRE_ID");
                String genreName = genreRows.getString("GENRE_NAME");
                Genre genre = new Genre(genreId, genreName);
                genres.add(idList, genre);
                idList++;
                Genre[] genres1 = new Genre[genres.size()];
                for (int i = 0; i < genres.size(); i++) {
                    genres1[i] = genres.get(i);
                }
                film.setGenres(genres1);
            }
            if(film.getGenres()==null){
                Genre[] g = new Genre[0];
                film.setGenres(g);
            }
            films.add(film);
        }
        return films;
    }

    @Override
    public Film getFilm(int id) {
        String sqlUpdate = "select FILM_ID from FILM";
        List<Integer> idList = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate, Integer.class));
        if (!idList.contains(id)) {
            throw new NotFoundException("id не найден");
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM as f left join MPA AS m on f.MPA_ID = m.MPA_ID where film_id = ?", id);
        if (filmRows.next()) {
            Film film = getFilmMethod(filmRows);
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE_FILM as gf left join GENRE AS g on gf.GENRE_ID = g.GENRE_ID where film_id = ?", id);
            List<Genre> genres = new ArrayList<>();
            while (genreRows.next()) {
                int genreId = genreRows.getInt("GENRE_ID");
                String genreName = genreRows.getString("GENRE_NAME");
                Genre genre = new Genre(genreId, genreName);
                genres.add(genre);
            }
            if(!genres.isEmpty()) {
                Genre[] genres1 = new Genre[genres.size()];
                for (int i = 0; i < genres.size(); i++) {
                    genres1[i] = genres.get(i);
                }
                film.setGenres(genres1);
                return film;
            }
            else {
                Genre[] g = new Genre[0];
                film.setGenres(g);
                return film;
            }
        } else {
            return null;
        }
    }

    public Film getFilmMethod(SqlRowSet filmRows) {
        Film film = new Film(
                Objects.requireNonNull(filmRows.getString("FILM_NAME")),
                Objects.requireNonNull(filmRows.getString("FILM_DESCRIPTION")),
                Objects.requireNonNull(filmRows.getDate("FILM_RELEASE_DATE")).toLocalDate(),
                filmRows.getInt("FILM_DURATION"));
        int filmId = filmRows.getInt("FILM_ID");
        film.setId(filmId);
        Integer mpaId = filmRows.getInt("MPA_ID");
        String mpaName = filmRows.getString("MPA_NAME");
        Mpa mpa = new Mpa(mpaId, mpaName);
        film.setMpa(mpa);

        String sqlUpdate2 = ("select USER_ID from LIKES where FILM_ID = " + id);
        List<Integer> idList2 = new ArrayList<>(jdbcTemplate.queryForList(sqlUpdate2, Integer.class));
        Set<Integer> likes = new HashSet<>(idList2);
        film.setLikes(likes);
        return film;
    }

/*
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE_FILM as gf left join GENRE AS g on gf.GENRE_ID = g.GENRE_ID where film_id = ?", id);
            List<Genre> genres = new ArrayList<>();
            while (genreRows.next()) {
                int genreId = genreRows.getInt("GENRE_ID");
                String genreName = genreRows.getString("GENRE_NAME");
                Genre genre = new Genre(genreId, genreName);
                genres.add(genre);
            }
            if(!genres.isEmpty()) {
                Genre[] genres1 = new Genre[genres.size()];
                for (int i = 0; i < genres.size(); i++) {
                    genres1[i] = genres.get(i);
                }
                film.setGenres(genres1);
            }
        return film;
    }

 */

    @Override
    public void updateFilmWithLikes(int id, int userId) {
        Film film = getFilm(id);
        if (!film.getLikes().contains(userId)) {
                /*Set<Integer> buffer = film.getLikes();
                buffer.add(userId);
                film.setLikes(buffer);

                 */
            String sqlQuery = "insert into LIKES(FILM_ID, USER_ID) " +
                    "values (?,?)";
            jdbcTemplate.update(sqlQuery, id, userId);

        } else {
            throw new ValidationException("пользователь уже поставил лайк");
        }
    }

    @Override
    public void deleteFilmWithLikes(int id, int userId) {
        Film film = getFilm(id);
        if (film.getLikes().contains(userId)) {
           /* Set<Integer> buffer = film.getLikes();
            buffer.remove(userId);
            film.setLikes(buffer);

            */
            String sqlQuery = "delete from LIKE where FILM_ID=? and USER_ID=? ";
            jdbcTemplate.update(sqlQuery, id, userId);
        } else {
            throw new ValidationException("пользователь не ставил лайк");
        }
    }

    public Genre[] getAllGenres(){
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE");
        List<Genre> genres = new ArrayList<>();
        while(genreRows.next()){
            int genreId = genreRows.getInt("GENRE_ID");
            String genreName = genreRows.getString("GENRE_NAME");
            Genre genre = new Genre(genreId, genreName);
            genres.add(genre);
        }
        Genre[] genres1 = new Genre[genres.size()];
        for (int i = 0; i < genres.size(); i++) {
            genres1[i] = genres.get(i);
        }
        return genres1;
    }


  public Genre getGenre(int id){
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select GENRE_NAME from GENRE where GENRE_ID=" + id);
        if(genreRows.next()) {
            String genreName = genreRows.getString("GENRE_NAME");
            return new Genre(id, genreName);
        }
        else{
            throw new NotFoundException(" ");
        }
    }

    public Mpa[] getAllMpa(){
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA");
        List<Mpa> mpas = new ArrayList<>();
        while(mpaRows.next()){
            int mpaId = mpaRows.getInt("MPA_ID");
            String mpaName = mpaRows.getString("MPA_NAME");
            Mpa mpa = new Mpa(mpaId, mpaName);
            mpas.add(mpa);
        }
        Mpa[] mpas1 = new Mpa[mpas.size()];
        for (int i = 0; i < mpas.size(); i++) {
            mpas1[i] = mpas.get(i);
        }
        return mpas1;
    }

    public Mpa getMpa(int id){
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select MPA_NAME from MPA where MPA_ID=" + id);
        if(mpaRows.next()) {
            String mpaName = mpaRows.getString("MPA_NAME");
            return new Mpa(id, mpaName);
        }
        else{
            throw new NotFoundException(" ");
        }
    }
}



