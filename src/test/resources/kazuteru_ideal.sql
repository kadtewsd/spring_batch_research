-- PostgreSQL 用の理想の クエリ
CREATE TABLE "kasakaid"."tb_trn_shop"  (
    shop_id serial NOT NULL PRIMARY KEY,
    shop_name CHARACTER VARYING(20),
    environment CHARACTER VARYING(200)
);
CREATE TABLE "kasakaid"."tb_trn_people"  (
    person_id serial NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name varchar(20)
--    first_name CHARACTER VARYING(20),
--    last_name CHARACTER VARYING(20)
);
CREATE TABLE "kasakaid"."tb_trn_customer"  (
    customer_id serial NOT NULL PRIMARY KEY,
    person_id int NOT NULL
);
CREATE TABLE "kasakaid"."tb_mst_shrine"  (
    id serial NOT NULL PRIMARY KEY,
    city VARCHAR(20),
    name varchar(20)
);

