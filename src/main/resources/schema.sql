CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) DEFAULT NULL,
    name VARCHAR(100),
    login VARCHAR(100) NOT NULL,
    birthday TIMESTAMP
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP (
    active_user_id int,
    passive_user_id int,
    is_accepted boolean DEFAULT FALSE,
    PRIMARY KEY (active_user_id, passive_user_id),
    FOREIGN KEY (active_user_id) REFERENCES users(id),
    FOREIGN KEY (passive_user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS MPA (
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE IF NOT EXISTS GENRES (
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE IF NOT EXISTS FILMS (
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(100),
    description text,
    release_date date,
    duration int,
    mpa int,
    rate int,
    FOREIGN KEY (mpa) REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS LIKES (
    user_id int,
    film_id int,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (film_id) REFERENCES films(id)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES (
    film_id int,
    genre_id int,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
);

