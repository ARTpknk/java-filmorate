# java-filmorate
Template repository for Filmorate project.

![Untitled (3)](https://user-images.githubusercontent.com/108333044/211214573-097e2d14-8fd6-4e0b-ac22-91c68899e30d.png)


Примеры использования базы данных:
Задача 1: выгрузить таблицу со столбацами (название фильма, жанр)
SELECT f.film_name, g.genre_name

FROM film AS f

LEFT JOIN genre AS g ON f.genre_id = g.genre_id

Задача 2: выгрузить id друзей пользователя с id=5
SELECT f.friend_id

FROM user AS u

LEFT JOIN friendship AS f ON u.user_id = f.user_id

WHERE user_id=5

Задача 3: выгрузить id пользователей, которые лайкнули фильм с id=3
SELECT l.user_id

FROM film AS f

LEFT JOIN like AS l ON f.film_id = l.film_id

WHERE f.film_id = 3
