DROP ROLE IF EXISTS abstract_reddit_user;

CREATE ROLE abstract_reddit_user WITH
    LOGIN
    SUPERUSER
    INHERIT
    CREATEDB
    CREATEROLE
    REPLICATION
    PASSWORD '1234';

-- Drop all tables
DROP TABLE IF EXISTS public."user" CASCADE;

-- SCHEMA: abstract_reddit_schema
DROP SCHEMA IF EXISTS public;
CREATE SCHEMA IF NOT EXISTS public
    AUTHORIZATION abstract_reddit_user;

CREATE TABLE IF NOT EXISTS public."user"
(
    id BIGSERIAL not null
        constraint user_pk
            primary key,
    created  timestamp,
    email    varchar(255),
    enabled  boolean,
    password varchar(255),
    username varchar(255)
);

alter table public."user"
    owner to abstract_reddit_user;
