drop table if exists t_menu;
drop table if exists t_order;
drop table if exists t_order_item;
drop table if exists t_tea_maker;
drop table if exists users;
drop table if exists authorities;

create table t_menu (
    id bigint not null auto_increment,
    create_time timestamp,
    name varchar(255),
    price bigint,
    size varchar(255),
    update_time timestamp,
    primary key (id)
);

create table t_order (
    id bigint not null auto_increment,
    amount_discount integer,
    amount_pay bigint,
    amount_total bigint,
    create_time timestamp,
    status integer,
    update_time timestamp,
    maker_id bigint,
    primary key (id)
);

create table t_order_item (
   item_id bigint not null,
   order_id bigint not null
);

create table t_tea_maker (
    id bigint not null auto_increment,
    create_time timestamp,
    name varchar(255),
    update_time timestamp,
    primary key (id)
);

create table users(
    username varchar(50) not null primary key,
    password varchar(500) not null,
    enabled boolean not null
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null
);

create unique index ix_auth_username on authorities (username, authority);
