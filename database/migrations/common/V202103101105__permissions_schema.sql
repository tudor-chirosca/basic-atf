create table role(
    id       varchar(36) not null primary key,
    function varchar(16)
);
create table ui_permission(
    id  varchar(36)  not null primary key,
    key varchar(150) unique not null
);

create table role_ui_permission(
    role_id          varchar(36),
    ui_permission_id varchar(36),
    participant_type  varchar(16),

    primary key (role_id, ui_permission_id, participant_type)
)
