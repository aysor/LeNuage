-- liquibase formatted sql

-- changeset Author:AyselS


grant all privileges on database cloud to netology_user;

grant connect on database cloud to netology_user;
grant select on all tables in schema public to netology_user;
grant all on schema public to netology_user;

create table if not exists users (
                                     id bigserial primary key not null,
                                     name varchar(255) unique not null,
                                     password varchar(255) not null
);
create table if not exists files (
                                     id bigserial primary key not null,
                                     file_name varchar(255) unique not null,
                                     file_data bytea,
                                     file_size bigint,
                                     user_id integer references users (id)
);