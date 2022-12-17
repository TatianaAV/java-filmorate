CREATE TABLE IF NOT EXISTS users (
    user_id    LONG   NOT NULL   PRIMARY KEY   AUTO_INCREMENT,
    username   varchar(255) not null,
    email      varchar(255) not null unique,
    login      varchar(255) not null,
    birthday   date
);


CREATE TABLE IF NOT EXISTS mpa (
     mpa_id LONG NOT NULL primary key AUTO_INCREMENT,
     name VARCHAR(50) not null unique
);

CREATE TABLE IF NOT EXISTS films (
    film_id LONG NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name_film varchar(255),
    description varchar(500),
    release_date date,
    duration long,
    rate int
   );

CREATE TABLE IF NOT EXISTS friends (
    user_id LONG NOT NULL REFERENCES users (user_id),
    friend_id LONG NOT NULL REFERENCES users (user_id),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id LONG REFERENCES films (film_id),
    users_id LONG REFERENCES users (user_id),
    PRIMARY KEY (users_id,film_id)
 );
CREATE TABLE IF NOT EXISTS genre (
    genre_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) not null unique,
    constraint GENRE_PK primary key (genre_id)
);


CREATE TABLE IF NOT EXISTS film_genre (
    film_id LONG REFERENCES films (film_id),
    genre_id LONG REFERENCES genre (genre_id)
   );


CREATE TABLE IF NOT EXISTS film_mpa (
    film_id LONG REFERENCES films (film_id),
    mpa_id LONG REFERENCES mpa (mpa_id)
);

