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

create table social.person
(
    id                 serial      not null primary key,
    first_name         varchar(64),
    last_name          varchar(128),
    birth_date         date,
    gender             gender,
    national_id        varchar(11) unique,
    address            varchar(256),
    phone_number       varchar(10) unique,
    user_id            integer
        constraint person_user_fk references security.user (id),

    created_by         varchar(32) not null,
    created_date       timestamp   not null default now(),
    last_modified_by   varchar(32) not null,
    last_modified_date timestamptz not null default now()
);

create table social.employee
(
    id                 serial primary key,
    person_id          integer           not null
        constraint employee_person_fk references social.person (id),
    position           varchar(255)      not null,
    salary             integer           not null,
    employment_type    employment_type   not null,
    status             employment_status not null,

    created_by         varchar(32)       not null,
    created_date       timestamp         not null default now(),
    last_modified_by   varchar(32)       not null,
    last_modified_date timestamptz       not null default now()
);


create table social.group
(
    id                 serial      not null primary key,
    name               varchar(64) not null unique,
    description        varchar(256),
    active             boolean     not null default true,
    privacy            privacy     not null default 'PUBLIC',
    owner_id           integer     not null
        constraint group_person_fk references social.person (id),

    created_by         varchar(32) not null,
    created_date       timestamp   not null default now(),
    last_modified_by   varchar(32) not null,
    last_modified_date timestamptz not null default now()
);

create table social.person_group
(
    id        serial  not null primary key,
    person_id integer not null
        constraint user_group_person_fk references social.person (id),
    group_id  integer not null
        constraint user_group_group_fk references social.group (id),

    constraint user_id_group_id_uk unique (person_id, group_id)
);

create table social.team
(
    id        serial  not null primary key,
    name      varchar(64) not null,
    manager_id integer not null
        constraint team_employee_fk references social.employee (id),
    description varchar(256)
);


create table social.employee_team
(
    id        serial  not null primary key,
    employee_id integer not null
        constraint employee_team_employee_fk references social.employee (id),
    team_id    integer not null
        constraint employee_team_team_fk references social.team (id),

    constraint employee_id_team_id_uk unique (employee_id, team_id)
);


INSERT INTO social.person (first_name, last_name, birth_date, gender, national_id, address, phone_number, "user_id","created_by","created_date","last_modified_by","last_modified_date")

VALUES ('Davit',
        'Tkeshelashvili',
        TO_DATE('08-02-2007', 'DD-MM-YYYY'),
        'MALE'::gender,
        '01611117972',
        'Atskuri str. 3A N27',
        '595902230',
        1,
        'Tkesho',
        '2023-10-01 12:00:00',
        'Tkesho',
        '2023-10-01 12:00:00'
        );