create type gender as enum ('MALE', 'FEMALE', 'OTHER');

create type privacy as enum ('PRIVATE', 'PUBLIC');

create type employment_type as enum (
    'full_time',
    'part_time',
    'contract',
    'intern',
    'temporary'
    );

create type employment_status as enum (
    'active',
    'on_leave',
    'terminated'
    );

create table social.person_detail
(
    id                 serial       not null primary key,
    first_name         varchar(64)  not null,
    last_name          varchar(128) not null,
    birth_date         date         not null,
    gender             gender       not null,
    national_id        varchar(11)  not null unique,
    address            varchar(256),
    phone_number       varchar(10)  not null unique,
    user_id            integer      not null
        constraint person_user_fk references security.user (id),

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);

create table social.employee
(
    id                 serial primary key,
    person_id          integer      not null
        constraint employee_person_details_fk references social.person_detail (id),
    position           varchar(255) not null,
    salary             integer      not null,
    employment_type    employment_type  not null,
    status             employment_status  not null,

    created_by         varchar(32)  not null,
    created_date       timestamp    not null default now(),
    last_modified_by   varchar(32)  not null,
    last_modified_date timestamptz  not null default now()
);


create table social.group
(
    id                 serial      not null primary key,
    name               varchar(64) not null unique,
    description        varchar(256),
    active             boolean     not null default true,
    privacy            privacy     not null default 'PUBLIC',
    owner_id           integer     not null
        constraint group_person_detail_fk references social.person_detail (id),

    created_by         varchar(32) not null,
    created_date       timestamp   not null default now(),
    last_modified_by   varchar(32) not null,
    last_modified_date timestamptz not null default now()
);

create table social.person_detail_group
(
    id       serial  not null primary key,
    user_id  integer not null
        constraint user_group_person_detail_fk references social.person_detail (id),
    group_id integer not null
        constraint user_group_group_fk references social.group (id),

    constraint user_id_group_id_uk unique (user_id, group_id)
)