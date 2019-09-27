--CREATE TABLE profiles (
--  id BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
--  name VARCHAR(255) NOT NULL,
--  project VARCHAR (255) NOT NULL,
--  created_date BIGINT(25) NOT NULL,
--  updated_date BIGINT(25) DEFAULT NULL
--);

CREATE TABLE user(
    user_id INT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    panther_id VARCHAR(9) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    student_access BIT NOT NULL,
    advisor_access BIT NOT NULL,
    admin_access BIT NOT NULL,
    program_id INT NOT NULL
);

CREATE TABLE fields(
    fields_id INT PRIMARY KEY,
    form_id INT NOT NULL,
    label VARCHAR(255) NOT NULL,
    data VARCHAR(255) NOT NULL,
    data_type INT NOT NULL
);

CREATE TABLE forms(
    form_id INT PRIMARY KEY,
    event_id INT NOT NULL,
    user_id INT NOT NULL
);