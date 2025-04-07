create schema "people";

create type gender as enum ('MALE', 'FEMALE', 'OTHER');

alter table auth.users
    add column person_id integer not null default 1;


create table people.persons
(
    id                   serial       not null primary key,
    first_name           varchar(64)  not null,
    last_name            varchar(128) not null,
    birth_date           date         not null,
    gender               gender       not null,
    national_id          varchar(11)  not null unique,
    address              varchar(256),
    phone_number         varchar(10)  not null unique,
    user_id              integer      not null constraint persons_users_fk references auth.users (id),

    "created_by"         varchar(32)  not null,
    "created_date"       timestamp    not null default now(),
    "last_modified_by"   varchar(32)  not null,
    "last_modified_date" timestamptz  not null default now()
);

create table people.groups
(
    id   serial      not null primary key,
    name varchar(64) not null
);

create table people.users_groups
(
    id       serial  not null primary key,
    user_id  integer not null
        constraint users_groups_users_fk references auth.users (id),
    group_id integer not null
        constraint users_group_groups_fk references people.groups (id),

    constraint user_id_group_id_uq unique (user_id, group_id)
)
