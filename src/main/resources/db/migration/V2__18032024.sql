create table public.subreddit_subscribers
(
    user_id      bigint not null,
    subreddit_id bigint not null,
    primary key (user_id, subreddit_id),
    constraint fk_subreddit_subscribers_subreddit_id FOREIGN KEY (subreddit_id) REFERENCES subreddit (id) ON DELETE CASCADE,
    constraint fk_subreddit_subscribers_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);

create index idx_subreddit_subscribers_user_id
    on public.subreddit_subscribers (user_id);

create index idx_subreddit_subscribers_subreddit_id
    on public.subreddit_subscribers (subreddit_id);

