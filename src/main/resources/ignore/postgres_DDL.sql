CREATE TABLE IF NOT EXISTS refresh_token
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT refresh_token_pk
            PRIMARY KEY,
    created_date timestamp,
    token        varchar(255)
);

ALTER TABLE refresh_token
    OWNER TO root;

CREATE TABLE IF NOT EXISTS "user"
(
    id       BIGSERIAL NOT NULL
        CONSTRAINT user_pk
            PRIMARY KEY,
    created  timestamp,
    email    varchar(255),
    enabled  boolean,
    password varchar(255),
    username varchar(255)
);

ALTER TABLE "user"
    OWNER TO root;

CREATE TABLE IF NOT EXISTS subreddit
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT subreddit_pk
            PRIMARY KEY,
    created_date timestamp,
    description  varchar(255),
    name         varchar(255),
    user_id      bigint
        CONSTRAINT subreddit_user_id_fk
            REFERENCES "user"
);

ALTER TABLE subreddit
    OWNER TO root;

CREATE TABLE IF NOT EXISTS post
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT post_pk
            PRIMARY KEY,
    created_date timestamp,
    description  varchar(255),
    post_name    varchar(255),
    url          varchar(255),
    vote_count   integer,
    subreddit_id bigint
        CONSTRAINT post_subreddit_id_fk
            REFERENCES subreddit,
    user_id      bigint
        CONSTRAINT post___fk
            REFERENCES "user"
);

ALTER TABLE post
    OWNER TO root;

CREATE TABLE IF NOT EXISTS comment
(
    id           BIGSERIAL NOT NULL
        CONSTRAINT comment_pk
            PRIMARY KEY,
    created_date timestamp,
    text         varchar(255),
    post_id      bigint
        CONSTRAINT comment_post_id_fk
            REFERENCES post,
    user_id      bigint
        CONSTRAINT comment_user_id_fk
            REFERENCES "user"
);

ALTER TABLE comment
    OWNER TO root;

CREATE TABLE IF NOT EXISTS subreddit_posts
(
    subreddit_id BIGSERIAL NOT NULL
        CONSTRAINT subreddit_posts_subreddit_id_fk
            REFERENCES subreddit,
    posts_id     bigint
        CONSTRAINT subreddit_posts_post_id_fk
            REFERENCES post
);

ALTER TABLE subreddit_posts
    OWNER TO root;

CREATE TABLE IF NOT EXISTS token
(
    id          BIGSERIAL NOT NULL
        CONSTRAINT token_pk
            PRIMARY KEY,
    expiry_date timestamp,
    token       varchar(255),
    user_id     bigint
        CONSTRAINT token_user_id_fk
            REFERENCES "user"
);

ALTER TABLE token
    OWNER TO root;

CREATE TABLE IF NOT EXISTS vote
(
    id        BIGSERIAL NOT NULL
        CONSTRAINT vote_pk
            PRIMARY KEY,
    vote_type smallint,
    post_id   bigint
        CONSTRAINT vote_post_id_fk
            REFERENCES post,
    user_id   bigint
        CONSTRAINT vote_user_id_fk
            REFERENCES "user"
);

ALTER TABLE vote
    OWNER TO root;

CREATE TABLE IF NOT EXISTS role
(
    id   BIGSERIAL,
    name VARCHAR(255),
    CONSTRAINT "role_pk" PRIMARY KEY (id),
    CONSTRAINT "uq_role_name" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id BIGSERIAL,
    role_id BIGSERIAL,
    CONSTRAINT "users_roles_pk" PRIMARY KEY (user_id, role_id),
    CONSTRAINT "user_role_user_id_fk" FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT "user_role_role_id_fk" FOREIGN KEY (role_id) REFERENCES "user" (id)
);