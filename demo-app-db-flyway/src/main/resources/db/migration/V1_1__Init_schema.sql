CREATE SCHEMA IF NOT EXISTS emp;

CREATE TABLE IF NOT EXISTS emp.EMPLOYEE (
        id int4 not null,
        email_id varchar(255),
        first_name varchar(255),
        last_name varchar(255),
        primary key (id)
 );
