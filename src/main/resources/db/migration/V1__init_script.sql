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
    username varchar(255),
    constraint uq_user_email unique (email)
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
    REFERENCES "user",
    constraint uq_post_url unique (url)
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
    vote_count   integer,
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
    vote_count   integer,
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
    comment_id bigint
    CONSTRAINT vote_comment_id_fk
    REFERENCES comment,
    user_id   bigint
        CONSTRAINT vote_user_id_fk
            REFERENCES "user"
);


ALTER TABLE vote
    OWNER TO root;

CREATE TABLE IF NOT EXISTS role
(
    id         BIGSERIAL,
    name       VARCHAR(255),
    created_at timestamp NOT NULL,
    CONSTRAINT "role_pk" PRIMARY KEY (id),
    CONSTRAINT "uq_role_name" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id BIGSERIAL,
    role_id BIGSERIAL,
    CONSTRAINT "users_roles_pk" PRIMARY KEY (user_id, role_id),
    CONSTRAINT "user_role_user_id_fk" FOREIGN KEY (user_id) REFERENCES "user" (id),
    CONSTRAINT "user_role_role_id_fk" FOREIGN KEY (role_id) REFERENCES role(id)
);

create table public.activation_token
(
    id          bigserial    not null,
    activated   boolean      not null default false,
    expiry_date timestamp(6) not null,
    token       varchar(36)  not null,
    user_id     bigint       not null,
    primary key (id),
    constraint idx_activation_token_token unique (token),
    constraint fk_activation_token_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
    CONSTRAINT "user_role_role_id_fk" FOREIGN KEY (role_id) REFERENCES role (id)
);


CREATE TABLE IF NOT EXISTS refresh_token
(
    id            BIGSERIAL,
    value         VARCHAR(255) NOT NULL,
    issued_at     timestamp NOT NULL,
    expiration_at timestamp NOT NULL,
    user_id       bigint,

    CONSTRAINT "refresh_token_pk" PRIMARY KEY (id),
    CONSTRAINT "refresh_token_user_fk" FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);

create index idx_comment_user_id
    on comment (user_id);

create index idx_comment_post_id
    on comment (post_id);

create index idx_post_postName
    on post (post_name);

create index idx_post_subreddit_id
    on post (subreddit_id);

create index idx_post_user_id
    on post (user_id);

create index idx_post_created_date
    on post (created_date);

create index idx_post_vote_count
    on post (vote_count);

create index idx_subreddit_name
    on subreddit (name);

create index idx_subreddit_user_id
    on subreddit (user_id);

create index idx_vote_user_id
    on vote (user_id);

create index idx_vote_post_id
    on vote (post_id);
CREATE INDEX refresh_token_expiration_at_idx
    ON refresh_token (expiration_at);