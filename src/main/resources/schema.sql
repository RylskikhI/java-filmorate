create table IF NOT EXISTS "user"
(
    user_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar NOT NULL,
    login varchar NOT NULL,
    name varchar NOT NULL,
    birthday date NOT NULL
);

create table IF NOT EXISTS "film"
(
    film_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar NOT NULL,
    description varchar NOT NULL,
    release_date date NOT NULL ,
    duration long NOT NULL,
    category_id integer NOT NULL
);

create table IF NOT EXISTS "user_friends"
(
    user_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    friend_id long,
    friendship_id integer
);

create table IF NOT EXISTS "user_films"
(
    user_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id long
);

create table IF NOT EXISTS "friendship"
(
    friendship_id integer,
    status varchar
);

create table IF NOT EXISTS "film_genre"
(
    film_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_id integer
);

create table IF NOT EXISTS "film_rating"
(
    rating_id long GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar
);

create table IF NOT EXISTS "genres"
(
    genre_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar
);


