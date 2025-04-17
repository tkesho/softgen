create schema audit;

create table audit.email
(
    id         serial primary key,
    from_email varchar(255) not null,
    to_email   varchar(255) not null,
    subject    varchar(255) not null,
    body       text         not null,
    sent_date  timestamp    not null default now()
);

create table audit.post_history
(
    id serial primary key,
    post_id integer not null,
    title varchar(255) not null,
    body varchar(1024) not null,
    owner_id integer not null,
    group_id integer not null,
    hidden boolean not null default false,

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);

create table audit.comment_history
(
    id serial primary key,
    comment_id integer not null,
    post_id integer not null constraint comment_post_fk references content.post (id),
    author_id integer not null constraint comment_person_detail_fk references social.person_detail (id),
    parent_id integer constraint comment_comment_fk references content.comment (id),
    body varchar(1024) not null,
    hidden boolean not null default false,

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);