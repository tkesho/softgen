-- CREATE TABLES

create schema auth;

create table auth.users
(
    "id"                 serial       not null primary key,
    "username"           varchar(32)  not null unique,
    "password"           varchar(256) not null,
    "email"              varchar(254) not null unique,
    "active"             boolean      not null default true,

    "created_by"         varchar(32)  not null,
    "created_date"       timestamp    not null default now(),
    "last_modified_by"   varchar(32)  not null,
    "last_modified_date" timestamptz  not null default now()
);

create table auth.roles
(
    "id"   serial      not null primary key,
    "role" varchar(32) not null
);

create table auth.users_roles
(
    id      serial  not null primary key,
    user_id integer not null
        constraint user_roles_user_fk references auth.users (id),
    role_id integer not null
        constraint user_roles_roles_fk references auth.roles (id),

    constraint user_id_role_id_uk unique (user_id, role_id)
);

create table auth.authorities
(
    id        serial      not null primary key,
    authority varchar(64) not null
);

create table auth.roles_authorities
(
    id           serial  not null primary key,
    role_id      integer not null
        constraint authority_roles_roles references auth.roles (id),
    authority_id integer not null
        constraint authority_roles_authorities references auth.authorities (id),

    constraint role_id_authority_id_uk unique (role_id, authority_id)
);


-- INSERT VALUES INTO TABLES

insert into auth.users (email, username, password, created_by, last_modified_by)
values ('admin@softgen.ge', 'admin', '$2a$10$./iK4EZY/vuUT4mT.ZqwuuxTWsv2/KCxPM8Ipi9WU3F5narCaIlGW', 'admin',
        'admin');

insert into auth.roles (role)
values ('ROLE_ADMIN'),
       ('ROLE_TEAM_LEADER');

insert into auth.authorities (authority)
values ('USER_READ'),
       ('USER_CREATE'),
       ('USER_UPDATE'),
       ('USER_DELETE'),
       ('PERSON_READ'),
       ('PERSON_CREATE'),
       ('PERSON_UPDATE'),
       ('PERSON_DELETE'),
       ('GROUP_DELETE'),
       ('GROUP_UPDATE'),
       ('GROUP_CREATE'),
       ('GROUP_READ_PUBLIC'),
       ('GROUP_READ_PRIVATE');

insert into auth.roles_authorities (role_id, authority_id)
select 1, a.id
from auth.authorities a;

insert into auth.users_roles (user_id, role_id)
select 1, a.id
from auth.roles a;