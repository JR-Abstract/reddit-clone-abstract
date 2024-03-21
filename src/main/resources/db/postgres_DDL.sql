create table if not exists refresh_token
(
    id           BIGSERIAL not null
    constraint refresh_token_pk
    primary key,
    created_date timestamp,
    token        varchar(255)
    );

alter table refresh_token
    owner to root;

create table if not exists "user"
(
    id       BIGSERIAL not null
    constraint user_pk
    primary key,
    created  timestamp,
    email    varchar(255),
    enabled  boolean,
    password varchar(255),
    username varchar(255),
    constraint uq_user_email unique (email)
    );

alter table "user"
    owner to root;

create table if not exists subreddit
(
    id           BIGSERIAL not null
    constraint subreddit_pk
    primary key,
    created_date timestamp,
    description  varchar(255),
    name         varchar(255),
    user_id      bigint
    constraint subreddit_user_id_fk
    references "user"
    );

alter table subreddit
    owner to root;

create table if not exists post
(
    id           BIGSERIAL not null
    constraint post_pk
    primary key,
    created_date timestamp,
    description  varchar(255),
    post_name    varchar(255),
    url          varchar(255),
    vote_count   integer,
    subreddit_id bigint
    constraint post_subreddit_id_fk
    references subreddit,
    user_id      bigint
    constraint post___fk
    references "user",
    constraint uq_post_url unique (url)
    );

alter table post
    owner to root;

create table if not exists comment
(
    id           BIGSERIAL not null
    constraint comment_pk
    primary key,
    created_date timestamp,
    text         varchar(255),
    post_id      bigint
    constraint comment_post_id_fk
    references post,
    user_id      bigint
    constraint comment_user_id_fk
    references "user"
    );

alter table comment
    owner to root;

create table if not exists subreddit_posts
(
    subreddit_id BIGSERIAL not null
    constraint subreddit_posts_subreddit_id_fk
    references subreddit,
    posts_id     bigint
    constraint subreddit_posts_post_id_fk
    references post
);

alter table subreddit_posts
    owner to root;

create table if not exists token
(
    id          BIGSERIAL not null
    constraint token_pk
    primary key,
    expiry_date timestamp,
    token       varchar(255),
    user_id     bigint
    constraint token_user_id_fk
    references "user"
    );

alter table token
    owner to root;

create table if not exists vote
(
    vote_id        BIGSERIAL not null
    constraint vote_pk
    primary key,
    vote_type smallint,
    post_id   bigint
    constraint vote_post_id_fk
    references post,
    user_id   bigint
    constraint vote_user_id_fk
    references "user"
);

alter table vote
    owner to root;

CREATE TABLE IF NOT EXISTS role
(
    id   BIGSERIAL,
    name VARCHAR(255),
    created_at timestamp not null,
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