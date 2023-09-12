# Java-Filmorate
### Перед вами бэкенд для сервиса, который работает с фильмами и оценками пользователей
#### Сервис хранит информацию о фильмах (название, описание, режиссёр, жанр) и о пользователях (контакты, друзья, любимые фильмы) 

В работе используются Spring Boot, SQL, Lombok, Jdbc Template. <br>

##### Для взаимодействия с сервисом созданы следующие эндпоинты:

__* Создать пользователя__ : POST http://localhost:8080/users  
{
"login": "dolore",
"name": "Nick Name",
"email": "mail@mail.ru",
"birthday": "1946-08-20"
} <br>
__* Получить пользователя__ : GET http://localhost:8080/users/{id} <br>
__* Удалить пользователя__ : DELETE http://localhost:8080/users/{id} <br>
__* Обновить пользователя__ : PUT http://localhost:8080/users  
{
"login": "doloreUpdate",
"name": "New Name",
"id": 1,
"email": "mail@yandex.ru",
"birthday": "1976-09-20"
} <br>
__* Получить всех пользователей__ : GET http://localhost:8080/users <br>
__* Добавить друга__ : PUT http://localhost:8080/users/{id}/friends/{friendId} <br>
__* Удалить друга__ : DELETE http://localhost:8080/users/{id}/friends/{friendId} <br>
__* Получить всех своих друзей__ : GET http://localhost:8080/users/{id}/friends <br>
__* Получить список общих друзей__ : GET http://localhost:8080/users/{id}/friends/common/{friendId} <br>
<br>

__* Добавить фильм__ : POST http://localhost:8080/films 
{
"name": "Film",
"description": "New Film",
"releaseDate": "1967-03-25",
"duration": 100,
"mpa": { "id": 1}
} <br>
__* Обновить фильм__ : PUT http://localhost:8080/films 
{
"id": 1,
"name": "Film Updated",
"releaseDate": "1989-04-17",
"description": "New film update decription",
"duration": 190,
"rate": 4,
"mpa": { "id": 2}
} <br>
__* Получить все фильмы__ : GET http://localhost:8080/films <br>
__* Получить фильм__ : GET http://localhost:8080/films/{id} <br>
__* Пользователь ставит лайк фильму__ : PUT http://localhost:8080/films/{id}/like/{userId} <br>
__* Пользователь удаляет лайк фильму__ : DELETE http://localhost:8080/films/{id}/like/{userId} <br>
__* Получить самые популярные фильмы__ : GET http://localhost:8080/films/popular ,not required query param: count (размер списка)<br>
<br>

Жанры и возрастные рейтинги заранее внесены в базу данных через data.sql <br>
__* Получить список жанров__ : GET http://localhost:8080/genres <br>
__* Получить информацию о жанре__ : GET http://localhost:8080/genres/{id} <br>
__* Получить список возрастных рейтингов__ : GET http://localhost:8080/mpa <br>
__* Получить информацию о возрастном рейтинге__ : GET http://localhost:8080/mpa/{id} <br>

Сервис использует базу данных H2
###
Схема базы данных <br>
![Untitled (6)](https://user-images.githubusercontent.com/108333044/212437828-44934522-5034-4e55-9409-2fd98950f141.png)
###
используется кодировка UTF-8