create table Users
(
    id         char(36),
    email      varchar(64) not null unique,
    password   varchar(64) not null,
    primary key (id)
);

insert into users (id, email, password)
values ('8aa99c86-494e-488f-8ae4-65abb91e92ee', 'user@example.com', 'password')