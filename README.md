# Java-Filmorate
### Перед вами бэкенд для сервиса, который работает с фильмами и оценками пользователей
#### Сервис хранит информацию о фильмах (название, описание, режиссёр, жанр) и о пользователях (контакты, друзья, любимые фильмы) 

В работе используются Spring Boot, SQL, Lombok, Jdbc Template. <br>

##### Для взаимодействия с сервисом созданы следующие эндпоинты:

* __Создать пользователя__ : POST http://localhost:8080/users  
{
"login": "dolore",
"name": "Nick Name",
"email": "mail@mail.ru",
"birthday": "1946-08-20"
} <br>
* __Получить пользователя__ : GET http://localhost:8080/users/{id} <br>
* __Удалить пользователя__ : DELETE http://localhost:8080/users/{id} <br>
* __Обновить пользователя__ : PUT http://localhost:8080/users  
{
"login": "doloreUpdate",
"name": "New Name",
"id": 1,
"email": "mail@yandex.ru",
"birthday": "1976-09-20"
} <br>
* __Получить всех пользователей__ : GET http://localhost:8080/users <br>
* __Добавить друга__ : PUT http://localhost:8080/users/{id}/friends/{friendId} <br>
* __Удалить друга__ : DELETE http://localhost:8080/users/{id}/friends/{friendId} <br>
* __Получить всех своих друзей__ : GET http://localhost:8080/users/{id}/friends <br>
* __Получить список общих друзей__ : GET http://localhost:8080/users/{id}/friends/common/{friendId} <br>
<br>

* __Добавить фильм__ : POST http://localhost:8080/films 
{
"name": "Film",
"description": "New Film",
"releaseDate": "1967-03-25",
"duration": 100,
"mpa": { "id": 1}
} <br>
* __Обновить фильм__ : PUT http://localhost:8080/films 
{
"id": 1,
"name": "Film Updated",
"releaseDate": "1989-04-17",
"description": "New film update decription",
"duration": 190,
"rate": 4,
"mpa": { "id": 2}
} <br>
* __Получить все фильмы__ : GET http://localhost:8080/films <br>
* __Получить фильм__ : GET http://localhost:8080/films/{id} <br>
* __Пользователь ставит лайк фильму__ : PUT http://localhost:8080/films/{id}/like/{userId} <br>
* __Пользователь удаляет лайк фильму__ : DELETE http://localhost:8080/films/{id}/like/{userId} <br>
* __Получить самые популярные фильмы__ : GET http://localhost:8080/films/popular ,not required query param: count (размер списка)<br>
<br>

Жанры и возрастные рейтинги заранее внесены в базу данных через data.sql <br>
* __Получить список жанров__ : GET http://localhost:8080/genres <br>
* __Получить информацию о жанре__ : GET http://localhost:8080/genres/{id} <br>
* __Получить список возрастных рейтингов__ : GET http://localhost:8080/mpa <br>
* __Получить информацию о возрастном рейтинге__ : GET http://localhost:8080/mpa/{id} <br>

Сервис использует базу данных H2
###
Схема базы данных <br>
![Untitled (6)](https://user-images.githubusercontent.com/108333044/212437828-44934522-5034-4e55-9409-2fd98950f141.png)
###
используется кодировка UTF-8