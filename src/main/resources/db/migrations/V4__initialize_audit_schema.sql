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