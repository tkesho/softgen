create schema "posts"

create table posts.files (
    id serial primary key,
    name varchar(255) not null,
    object_key varchar(255) not null,
    content_type varchar(100),
    size integer not null,

    "created_by"         varchar(32) not null,
    "created_date"       timestamp   not null default now(),
    "last_modified_by"   varchar(32) not null,
    "last_modified_date" timestamptz not null default now()
);

create table posts.posts
(
    id serial primary key,
    title varchar(255) not null,
    body varchar(1024) not null,
    user_id integer not null constraint posts_users_fk references auth.users (id),
    group_id integer not null constraint posts_groups_fk references people.groups (id),
    hidden boolean not null default false,

    "created_by"         varchar(32) not null,
    "created_date"       timestamp   not null default now(),
    "last_modified_by"   varchar(32) not null,
    "last_modified_date" timestamptz not null default now()
);

alter table posts.files
    add column post_id integer not null default 0 constraint files_posts_fk references posts.posts (id)