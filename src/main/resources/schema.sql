DROP TABLE IF EXISTS comments cascade;
DROP TABLE IF EXISTS requests cascade;
DROP TABLE IF EXISTS bookings cascade;
DROP TABLE IF EXISTS items cascade;
DROP TABLE IF EXISTS users cascade;

CREATE TABLE users
(
    user_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name    varchar(80)                                  NOT NULL,
    email   varchar(80)                                  NOT NULL UNIQUE
);

CREATE TABLE items
(
    item_id     int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name        varchar(80)                                  NOT NULL,
    description varchar(500),
    available   boolean                                      NOT NULL,
    owner_id    int REFERENCES users (user_id),
    request_id  int
);

CREATE TABLE bookings
(
    booking_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    start_time timestamp without time zone                  NOT NULL,
    end_time   timestamp without time zone                  NOT NULL,
    item_id    int REFERENCES items (item_id)               NOT NULL,
    booker_id  int REFERENCES users (user_id)               NOT NULL,
    status     varchar(20)                                  NOT NULL
);

CREATE TABLE requests
(
    request_id   int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    description  varchar(80),
    requester_id int REFERENCES users (user_id)               NOT NULL
);

CREATE TABLE comments
(
    comment_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    text       varchar(600)                                 NOT NULL,
    item_id    int REFERENCES items (item_id)               NOT NULL,
    author_id  int REFERENCES users (user_id)               NOT NULL,
    created    timestamp without time zone
);

