create table user_audit_details
(
    id                         varchar(36)                         not null,
    activity_id                varchar(36)                         not null,
    timestamp                  timestamp                           not null,
    customer                   varchar(10),
    ips_suite_application_name varchar(5),
    channel                    varchar(20)                         not null,
    ip_address                 varchar(20),
    username                   varchar(50)                         not null,
    employer_or_representation varchar(10),
    user_role_list             varchar(100),
    user_activity_string       varchar(36)                         not null,
    correlation_id             varchar(20)                         not null,
    service_id                 number generated always as identity not null,
    approval_request_id        varchar(5)                          not null,
    request_or_response_enum   varchar(10)                         not null,
    request_url                varchar(50)                         not null,
    contents                   varchar(1000)                       not null,
    user_id                    varchar(10)                         not null,
    first_name                 varchar(15)                         not null,
    last_name                  varchar(20)                         not null,
    participant_id             varchar(10)                         not null
);

comment on column user_audit_details.id is 'User activity identifier UUID.';
comment on column user_audit_details.timestamp is 'Timestamp of the user audit data piece captured (request or response).';
comment on column user_audit_details.customer is 'Customer or regulating entity included in service/software sale contract.';
comment on column user_audit_details.ips_suite_application_name is 'Internal real-time product.';
comment on column user_audit_details.channel is 'Channel associated to the origin of the request. ';
comment on column user_audit_details.ip_address is 'IP address from the original requester.';
comment on column user_audit_details.username is 'Username used in the authentication system.';
comment on column user_audit_details.employer_or_representation is 'Generally, scheme or participant bank the user is associated with.';
comment on column user_audit_details.user_role_list is 'Comma separated list of user roles.';
comment on column user_audit_details.user_activity_string is 'User activity from the functional list.';
comment on column user_audit_details.correlation_id is 'UUID formatted id, uniquely marking an entire end-to-end flow in the system.';
comment on column user_audit_details.service_id is 'UI friendly version of the correlation id.';
comment on column user_audit_details.approval_request_id is 'A linking id into the workflow item generated within the "maker/checker" engine. only applies to activities subject to approval.';
comment on column user_audit_details.request_or_response_enum is 'String flagging the nature of the audit data piece.';
comment on column user_audit_details.request_url is 'In case of a request_or_response_enum = request, a destination url is expected';
comment on column user_audit_details.contents is 'Json content of a request or a response data piece - specific for each user activity''s api requests and responses';
comment on column user_audit_details.user_id is 'User identifier.';
comment on column user_audit_details.first_name is 'Users first name.';
comment on column user_audit_details.last_name is 'Users last name.';
comment on column user_audit_details.participant_id is 'Participant identifier.';

alter table user_audit_details
    add (
        constraint uad_pk
            primary key
                (id));

alter table user_audit_details
    add (
        constraint ai_fk
            foreign key
                (activity_id)
                references user_activity (id));
