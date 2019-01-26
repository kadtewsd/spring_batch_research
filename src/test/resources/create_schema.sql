CREATE TABLE tb_trn_shop  (
    shop_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    shop_name CHARACTER VARYING(20),
    environment CHARACTER VARYING(200)
);
CREATE TABLE tb_trn_people  (
    person_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(20),
    last_name varchar(20)
--    first_name CHARACTER VARYING(20),
--    last_name CHARACTER VARYING(20)
);
CREATE TABLE tb_trn_customer  (
    customer_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    person_id int NOT NULL,
--    first_name CHARACTER VARYING(20),
--    last_name CHARACTER VARYING(20)
);
CREATE TABLE tb_mst_shrine  (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(20),
    name varchar(20)
);
