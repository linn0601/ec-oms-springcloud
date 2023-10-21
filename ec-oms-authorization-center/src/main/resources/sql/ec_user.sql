-- auto-generated definition
create table user_
(
    id           bigint auto_increment
        primary key,
    date_created datetime(6)  null,
    date_updated datetime(6)  null,
    extra_info   varchar(255) null,
    password     varchar(255) null,
    username     varchar(255) null,
    constraint UKwqsqlvajcne4rlyosglqglhk
        unique (username)
);