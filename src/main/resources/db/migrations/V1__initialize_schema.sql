create table public.users
(
    "id"                 serial       not null primary key,
    "username"           varchar(32)  not null unique,
    "password"           varchar(256) not null,
    "email"              varchar(254) not null unique,
    "active"             boolean      not null default true,
    "role_id"            integer      not null default 1,

    "created_by"         varchar(32)  not null,
    "created_date"       timestamp    not null default now(),
    "last_modified_by"   varchar(32)  not null,
    "last_modified_date" timestamptz  not null default now()
);

insert into public.users (email, username, password, created_by, last_modified_by)
values('admin@softgen.ge', 'admin', '$2a$10$./iK4EZY/vuUT4mT.ZqwuuxTWsv2/KCxPM8Ipi9WU3F5narCaIlGW', 'admin',
       'admin');