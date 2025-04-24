-- CREATE TABLES

create schema security;
create schema content;
create schema social;

create table security.user
(
    id                 serial       not null primary key,
    username           varchar(32)  not null unique,
    password           varchar(256) not null,
    email              varchar(254) not null unique,
    active             boolean      not null default true,

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);

create table security.authority
(
    id        serial      not null primary key,
    name varchar(64) not null
);

create table security.user_authority
(
    id serial not null primary key,
    user_id integer not null
        constraint user_authority_user_fk references security.user (id),
    authority_id integer not null
        constraint user_authority_authority_fk references security.authority (id),

    constraint user_id_authority_id_uk unique (user_id, authority_id)
);

-- INSERT VALUES INTO TABLES

insert into security.user (email, username, password, created_by, last_modified_by)
values ('admin@softgen.ge', 'admin', '{bcrypt}$2a$10$./iK4EZY/vuUT4mT.ZqwuuxTWsv2/KCxPM8Ipi9WU3F5narCaIlGW', 'admin',
        'admin');


insert into security.authority (name)
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
       ('GROUP_READ_PRIVATE'),
       ('POST_READ'),
       ('POST_CREATE'),
       ('POST_UPDATE'),
       ('POST_DELETE'),
       ('EMAIL_READ'),
       ('EMAIL_CREATE'),
       ('EMAIL_UPDATE'),
       ('EMAIL_DELETE'),
       ('COMMENT_READ'),
       ('COMMENT_CREATE'),
       ('COMMENT_UPDATE'),
       ('COMMENT_DELETE'),
       ('EMPLOYEE_READ'),
       ('EMPLOYEE_CREATE'),
       ('EMPLOYEE_UPDATE'),
       ('EMPLOYEE_DELETE'),
       ('ROLE_ADMIN'),
       ('ROLE_TEAM_LEADER'),
       ('ROLE_EMPLOYEE');

insert into security.user_authority (user_id, authority_id)
select 1, id
from security.authority;