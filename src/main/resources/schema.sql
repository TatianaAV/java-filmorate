create table if not exists GENRE
(
    GENRE_ID INTEGER auto_increment,
    NAME     CHARACTER VARYING(50) not null
        unique,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table if not exists MPA
(
    MPA_ID BIGINT auto_increment
        primary key,
    NAME   CHARACTER VARYING(50) not null
        unique
);

create table if not exists FILMS
(
    FILM_ID      BIGINT auto_increment
        primary key,
    NAME_FILM    CHARACTER VARYING(255),
    DESCRIPTION  CHARACTER VARYING(500),
    RELEASE_DATE DATE,
    DURATION     BIGINT,
    RATE         INTEGER,
    MPA_ID       INTEGER not null
        references MPA
);

create table if not exists FILM_GENRE
(
    FILM_ID  BIGINT
        references FILMS
            on delete cascade,
    GENRE_ID BIGINT
        references GENRE
            on delete cascade
);

create table if not exists USERS
(
    USER_ID   BIGINT auto_increment
        primary key,
    USER_NAME CHARACTER VARYING(255) not null,
    EMAIL     CHARACTER VARYING(255) not null
        unique,
    LOGIN     CHARACTER VARYING(255) not null,
    BIRTHDAY  DATE
);

create table if not exists FRIENDS
(
    USER_ID   BIGINT not null
        references USERS
            on delete cascade,
    FRIEND_ID BIGINT not null
        references USERS
            on delete cascade,
    primary key (USER_ID, FRIEND_ID)
);

create table if not exists LIKES
(
    FILM_ID BIGINT not null
        references FILMS
            on delete cascade,
    USER_ID BIGINT not null
        references USERS
            on delete cascade,
    primary key (USER_ID, FILM_ID)
);


