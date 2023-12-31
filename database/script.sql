CREATE DATABASE belajar_spring_restful_api;

 CREATE TABLE users (
	username  			VARCHAR(100) NOT NULL,
	password 			VARCHAR(100) NOT NULL,
	name 				VARCHAR(100) NOT NULL, 
	token 				VARCHAR(100),
	token_expired_at 	BIGINT,
	created_by          varchar(100),
	created_date        varchar(50),
	modified_by         varchar(100),
	modified_date       varchar(100),
	PRIMARY KEY (username),
	UNIQUE (token)
)

select * from users;
desc users;

CREATE table contacts (
	id 			        varchar(100) not null,
	username 	        varchar(100) not null,
	first_name 	        varchar(100) not null,
	last_name 	        varchar(100),
	email 		        varchar(100),
	phone 		        varchar(100),
	created_by          varchar(100),
    created_date        varchar(50),
    modified_by         varchar(100),
    modified_date       varchar(100),
	primary key (id),
	foreign key fk_users_contacts (username) references users (username)
)

select * from contacts;
desc contacts;

create table addresses (
	id 			        varchar(100) not null,
	contact_id 	        varchar(100) not null,
	street 		        varchar(200),
	city 		        varchar(100),
	province 	        varchar(100),
	country 	        varchar(100) not null,
	postal_code         varchar(10),
	created_by          varchar(100),
    created_date        varchar(50),
    modified_by         varchar(100),
    modified_date       varchar(100),
	primary key (id),
	foreign key fk_contacts_addresses (contact_id) references contacts(id)
)

select * from addresses;
desc addresses;

-- DELETE FROM addresses ;
-- DELETE FROM contacts ;
-- DELETE FROM users ;

SELECT * FROM users u ;
SELECT * FROM contacts c ;
SELECT * FROM addresses a ;

SELECT u.username , u.token ,
	c.id , c.first_name , c.last_name , c.email , c.phone ,
	a.id , a.street , a.city , a.province , a.country , a.postal_code 
from users u 
join contacts c on c.username = u.username 
join addresses a on a.contact_id = c.id 
-- WHERE c.first_name = ""
;