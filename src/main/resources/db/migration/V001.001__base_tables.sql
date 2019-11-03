CREATE TABLE users(
    id           BIGINT(20) AUTO_INCREMENT,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    panther_id   VARCHAR(9)   NOT NULL,
    email        VARCHAR(255) NOT NULL,
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE form_defs(
    id BIGINT(20) AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles(
    id BIGINT(20) AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE field_defs(
    id BIGINT(20) AUTO_INCREMENT,
    form_def_id BIGINT(20) NOT NULL,
    label VARCHAR(255) NOT NULL,
    field_index INT NOT NULL,
    input_type VARCHAR(255) NOT NULL,
    data_type VARCHAR(255) NOT NULL,
    created_date BIGINT(25) NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (form_def_id) REFERENCES form_defs(id)
);

CREATE TABLE forms(
    id BIGINT(20) AUTO_INCREMENT,
    form_def_id BIGINT(20) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    approved BOOLEAN DEFAULT FALSE,
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (form_def_id) REFERENCES form_defs(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE fields(
    id BIGINT(20) AUTO_INCREMENT,
    field_def_id BIGINT(20) NOT NULL,
    form_id BIGINT(20) NOT NULL,
    data    VARCHAR(1024) NOT NULL,
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (field_def_id) REFERENCES field_defs(id),
    FOREIGN KEY (form_id) REFERENCES forms(id)
);

CREATE TABLE user_roles(
    id BIGINT(20) AUTO_INCREMENT,
    user_id BIGINT(20) NOT NULL,
    role_id BIGINT(20) NOT NULL,
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE role_authorities(
    id BIGINT(20) AUTO_INCREMENT,
    role_id BIGINT(20) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Test data used for persisting a user, role, user_role, and role_authorities

-- INSERT INTO users (
--   first_name,
--   last_name,
--   password,
--   panther_id,
--   email,
--   created_date
-- ) VALUES (
--   'fname',
--   'lname',
--   '$2a$10$bWmltslQK8lyy.lQJ9JOV.2auLDczsfWhrQb4Jg.mrC8j8zVuepPa',
--   'p_id',
--   'email',
--   1
-- );
--
-- INSERT INTO roles (
--   name,
--   description,
--   created_date
-- ) VALUES (
--   'test role',
--   'role description',
--   1
-- );
--
-- INSERT INTO user_roles (
--   user_id,
--   role_id,
--   created_date
-- ) VALUES (
--   1,
--   1,
--   1
-- );
--
-- INSERT INTO role_authorities (
--   authority,
--   role_id,
--   created_date
-- ) VALUES (
--   'TEST',
--   1,
--   1
-- ),(
--   'TEST2',
--   1,
--   1
-- );