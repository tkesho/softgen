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
    user_id integer not null constraint post_person_detail_fk references social.person_detail (id),
    group_id integer not null constraint post_group_fk references social.group (id),
    hidden boolean not null default false,

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);