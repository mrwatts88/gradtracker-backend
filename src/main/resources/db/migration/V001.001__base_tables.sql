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