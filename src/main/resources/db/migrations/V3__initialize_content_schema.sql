create table content.file (
    id serial primary key,
    name varchar(255) not null,
    object_key varchar(255) not null,
    content_type varchar(100),
    size integer not null,

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);

create table content.post
(
    id serial primary key,
    title varchar(255) not null,
    body varchar(1024) not null,
    owner_id integer not null constraint post_person_detail_fk references social.person_detail (id),
    group_id integer not null constraint post_group_fk references social.group (id),
    hidden boolean not null default false,

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);

create table content.comment
(
    id serial primary key,
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

create table content.post_file
(
    id serial primary key,
    post_id integer not null constraint post_file_post_fk references content.post (id),
    file_id integer not null constraint post_file_file_fk references content.file (id),

    constraint post_id_file_id_uk unique (post_id, file_id)
);