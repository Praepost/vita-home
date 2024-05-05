create sequence "hibernate_sequence" start 1 increment 1;

create table "role" (
    "id" int8 not null,
    "name" varchar(20),
    primary key ("id"));

create table "statuses" (
    "id" int8 not null,
    "name" varchar(255),
    primary key ("id"));

create table "task" (
    "id" int8 not null,
    "author" varchar(255),
    "message" varchar(255),
    "name" varchar(255),
    "timestamp" int8,
    primary key ("id"));

create table "task_statuses" (
    "task_id" int8 not null,
    "statuses_id" int8 not null);

create table "user" (
    "id" int8 not null,
    "active" boolean not null,
    "password" varchar(255),
    "timestamp" timestamp,
    "username" varchar(255),
    primary key ("id"));

create table "user_roles" (
    "user_id" int8 not null,
    "roles_id" int8 not null,
    primary key ("user_id", "roles_id"));

create table "task_author" (
     "task_id" int8 not null,
     "author_id" int8 not null,
     primary key ("author_id", "task_id"));

alter table "task_author" add constraint "task_user_fk" foreign key ("author_id") references "user";
alter table "task_author" add constraint "task_user_fk2" foreign key ("task_id") references "task";
alter table "task_statuses" add constraint "task_status_fk" foreign key ("statuses_id") references "statuses";
alter table "task_statuses" add constraint "task_status_fk2" foreign key ("task_id") references "task";
alter table "user_roles" add constraint "user_roles_fk" foreign key ("roles_id") references "role";
alter table "user_roles" add constraint "user_roles_fk2" foreign key ("user_id") references "user";