DROP TABLE IF EXISTS users;

CREATE SEQUENCE users_id_seq;
CREATE TABLE users (
  id BIGINT DEFAULT users_id_seq.nextval PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL
);