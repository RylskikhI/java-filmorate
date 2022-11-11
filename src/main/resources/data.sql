MERGE INTO genres (id, name)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');

MERGE INTO mpa (id, name)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

DELETE FROM LIKES;
DELETE FROM FILM_GENRES;
DELETE FROM FRIENDSHIP;
DELETE FROM USERS;
DELETE FROM FILMS;
ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1;


