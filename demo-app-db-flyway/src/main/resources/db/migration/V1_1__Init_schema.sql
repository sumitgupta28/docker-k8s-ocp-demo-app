CREATE SEQUENCE hibernate_sequence START 1;


CREATE TABLE IF NOT EXISTS EMPLOYEE (
        id int4 not null,
        email_id varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        primary key (id)
 );
