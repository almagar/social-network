create table if not exists User
(
    id          uuid,
    email       varchar(64) not null unique,
    username    varchar(64) not null unique,
    password    varchar(64) not null,
    description varchar(2056),
    avatar      blob,
    primary key (id)
);

create table if not exists Post
(
    id          uuid,
    userId      uuid,
    text        varchar(2056) not null,
    image       blob,
    createdAt   timestamp,
    updatedAt   timestamp,
    primary key (id),
    foreign key (userId) references User(id) on delete cascade
);

create table if not exists PostReaction
(
    postId      uuid,
    userId      uuid,
);
