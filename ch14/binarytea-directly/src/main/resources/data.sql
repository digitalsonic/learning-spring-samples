insert into t_menu (name, size, price, create_time, update_time) values ('Java咖啡', 'MEDIUM', 1200, now(), now());
insert into t_menu (name, size, price, create_time, update_time) values ('Java咖啡', 'LARGE', 1500, now(), now());

insert into t_tea_maker (name, create_time, update_time) values ('LiLei', now(), now());
insert into t_tea_maker (name, create_time, update_time) values ('HanMeimei', now(), now());

insert into t_order (maker_id, status, amount_discount, amount_pay, amount_total, create_time, update_time) values (1, 0, 100, 1200, 1200, now(), now());

insert into t_order_item (order_id, item_id) values (1, 1);

insert into users (username, password, enabled) values ('LiLei', '{bcrypt}$2a$10$iAty2GrJu9WfpksIen6qX.vczLmXlp.1q1OHBxWEX8BIldtwxHl3u', true);
insert into authorities (username, authority) values ('LiLei', 'ROLE_TEA_MAKER');

insert into users (username, password, enabled) values ('ZhangSan', '{bcrypt}$2a$10$iAty2GrJu9WfpksIen6qX.vczLmXlp.1q1OHBxWEX8BIldtwxHl3u', true);
insert into authorities (username, authority) values ('ZhangSan', 'ROLE_USER');
