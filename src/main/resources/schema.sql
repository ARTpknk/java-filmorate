DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS film CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS genre_film CASCADE;

CREATE TABLE IF NOT EXISTS genre (
    	genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    	genre_name VARCHAR
    );

CREATE TABLE IF NOT EXISTS mpa (
    	mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    	mpa_name VARCHAR
    );

CREATE TABLE IF NOT EXISTS film (
	film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	mpa_id INTEGER REFERENCES mpa(mpa_id),
	film_release_date DATE,
    film_duration INTEGER,
    film_name VARCHAR NOT NULL,
    film_description VARCHAR(200)
	);

	CREATE TABLE IF NOT EXISTS genre_film (
	    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        genre_id INTEGER REFERENCES genre(genre_id),
        film_id INTEGER REFERENCES film(film_id)
        );

CREATE TABLE IF NOT EXISTS users (
        user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        user_name VARCHAR,
        user_email VARCHAR,
        user_login VARCHAR,
        user_birthday DATE
        );

CREATE TABLE IF NOT EXISTS friendship (
        friendship_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    	first_friend_id INTEGER REFERENCES users(user_id),
    	second_friend_id INTEGER REFERENCES users(user_id)
    );

    CREATE TABLE IF NOT EXISTS likes (
        	like_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        	film_id INTEGER REFERENCES film(film_id),
        	user_id INTEGER REFERENCES USERS(user_id)
    );

       CREATE INDEX IF NOT EXISTS likes_index ON likes (film_id);
       CREATE INDEX IF NOT EXISTS likes_index2 ON likes (user_id);
       CREATE INDEX IF NOT EXISTS film_index ON film (mpa_id);
       CREATE INDEX IF NOT EXISTS genre_index ON genre_film (genre_id);
       CREATE INDEX IF NOT EXISTS genre_index_2 ON genre_film (film_id);

       CREATE UNIQUE INDEX IF NOT EXISTS like_id_index ON likes (like_id);
       CREATE UNIQUE INDEX IF NOT EXISTS film_id_index ON film (film_id);
       CREATE UNIQUE INDEX IF NOT EXISTS user_id_index ON users (user_id);
       CREATE UNIQUE INDEX IF NOT EXISTS friendship_id_index ON friendship (friendship_id);
       CREATE UNIQUE INDEX IF NOT EXISTS genre_id_index ON genre (genre_id);
       CREATE UNIQUE INDEX IF NOT EXISTS mpa_id_index ON mpa (mpa_id);
       CREATE UNIQUE INDEX IF NOT EXISTS genre_film_id_index ON genre_film(ID);













