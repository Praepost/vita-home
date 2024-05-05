insert into "user" ("id", "username", "password", "active")
values (1, 'Пользователь', 'Пользователь', true);

insert into "user" ("id", "username", "password", "active")
values (2, 'Оператор', 'Оператор', true);

insert into "user" ("id", "username", "password", "active")
values (3, 'Администратор', 'Администратор', true);

insert into "user_roles" ("user_id", "roles_id")
values (1, 1);

insert into "user_roles" ("user_id", "roles_id")
values (2, 2);

insert into "user_roles" ("user_id", "roles_id")
values (3, 3);


