CREATE TABLE users(
    id           BIGINT(20) AUTO_INCREMENT,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    panther_id   VARCHAR(9)   NOT NULL,
    email        VARCHAR(255) NOT NULL,
    enabled      BIT,
    is_account_non_expired BIT,
    is_account_non_locked BIT,
    is_credentials_non_expired BIT,
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
    approved BOOLEAN DEFAULT NULL,
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
    PRIMARY KEY (id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE degree_programs(
    id BIGINT(20) AUTO_INCREMENT,
    name VARCHAR(255),
    description VARCHAR (1024),
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE degree_program_states(
    id BIGINT(20) AUTO_INCREMENT,
    degree_program_id BIGINT(20),
    name VARCHAR(255),
    description VARCHAR (1024),
    created_date BIGINT(25)   NOT NULL,
    updated_date BIGINT(25) DEFAULT NULL,
    PRIMARY KEY(id)
);