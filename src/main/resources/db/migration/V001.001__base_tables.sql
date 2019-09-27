--CREATE TABLE profiles (
--  id BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
--  name VARCHAR(255) NOT NULL,
--  project VARCHAR (255) NOT NULL,
--  created_date BIGINT(25) NOT NULL,
--  updated_date BIGINT(25) DEFAULT NULL
--);

CREATE TABLE user(
    user_id int PRIMARY KEY
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    pantherid VARCHAR(9) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    student_access BIT NOT NULL,
    advisor_access BIT NOT NULL,
    admin_access BIT NOT NULL,
    program_id int
);