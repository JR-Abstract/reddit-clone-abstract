-- Db user

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
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS refresh_token CASCADE;
DROP TABLE IF EXISTS subreddit CASCADE;
DROP TABLE IF EXISTS post CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS subreddit_posts CASCADE;
DROP TABLE IF EXISTS token CASCADE;
DROP TABLE IF EXISTS vote CASCADE;

-- SCHEMA: public
DROP SCHEMA IF EXISTS public;
CREATE SCHEMA IF NOT EXISTS public
    AUTHORIZATION abstract_reddit_user;

-- Tables
CREATE TABLE IF NOT EXISTS "user"
(
    id       BIGSERIAL NOT NULL
        CONSTRAINT user_pk
            PRIMARY KEY,
    created  TIMESTAMP,
    email    VARCHAR(255),
    enabled  BOOLEAN,
    password VARCHAR(255),
    username VARCHAR(255)
);

ALTER TABLE "user"
    OWNER TO abstract_reddit_user;

CREATE TABLE IF NOT EXISTS refresh_token
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT refresh_token_pk
            PRIMARY KEY,
    created_date TIMESTAMP,
    token        VARCHAR(255)
);

ALTER TABLE refresh_token
    OWNER TO abstract_reddit_user;

CREATE TABLE IF NOT EXISTS subreddit
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT subreddit_pk
            PRIMARY KEY,
    created_date TIMESTAMP,
    description  VARCHAR(255),
    name         VARCHAR(255),
    user_id      BIGINT
        CONSTRAINT subreddit_user_id_fk
            REFERENCES "user"
);

ALTER TABLE subreddit
    OWNER TO abstract_reddit_user;

CREATE TABLE IF NOT EXISTS post
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT post_pk
            PRIMARY KEY,
    created_date TIMESTAMP,
    description  VARCHAR(255),
    post_name    VARCHAR(255),
    url          VARCHAR(255),
    vote_count   INTEGER,
    subreddit_id BIGINT
        CONSTRAINT post_subreddit_id_fk
            REFERENCES subreddit,
    user_id      BIGINT
        CONSTRAINT post___fk
            REFERENCES "user"
);

ALTER TABLE post
    OWNER TO abstract_reddit_user;

CREATE TABLE IF NOT EXISTS comment
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT comment_pk
            PRIMARY KEY,
    created_date TIMESTAMP,
    text         VARCHAR(255),
    post_id      BIGINT
        CONSTRAINT comment_post_id_fk
            REFERENCES post,
    user_id      BIGINT
        CONSTRAINT comment_user_id_fk
            REFERENCES "user"
);

ALTER TABLE comment
    OWNER TO abstract_reddit_user;

CREATE TABLE IF NOT EXISTS subreddit_posts
(
    subreddit_id BIGSERIAL NOT NULL
        CONSTRAINT subreddit_posts_subreddit_id_fk
            REFERENCES subreddit,
    posts_id     BIGINT
        CONSTRAINT subreddit_posts_post_id_fk
            REFERENCES post
);

ALTER TABLE subreddit_posts
    OWNER TO abstract_reddit_user;

CREATE TABLE IF NOT EXISTS token
(
    id          BIGSERIAL NOT NULL
        CONSTRAINT token_pk
            PRIMARY KEY,
    expiry_date TIMESTAMP,
    token       VARCHAR(255),
    user_id     BIGINT
        CONSTRAINT token_user_id_fk
            REFERENCES "user"
);

ALTER TABLE token
    OWNER TO abstract_reddit_user;

CREATE TABLE IF NOT EXISTS vote
(
    id        BIGSERIAL NOT NULL
        CONSTRAINT vote_pk
            PRIMARY KEY,
    vote_type SMALLINT,
    post_id   BIGINT
        CONSTRAINT vote_post_id_fk
            REFERENCES post,
    user_id   BIGINT
        CONSTRAINT vote_user_id_fk
            REFERENCES "user"
);

ALTER TABLE vote
    OWNER TO abstract_reddit_user;
