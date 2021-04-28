create table user_activity
(
    id          varchar(36) not null,
    name        varchar(30) not null
);

alter table user_activity
    add (
        constraint ua_pk
            primary key
                (id));
