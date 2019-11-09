--STATEMENT createUser
INSERT INTO users (
  first_name,
  last_name,
  password,
  panther_id,
  email,
  created_date,
  isAccountNonExpired,
  isAccountNonLocked,
  isCredentialsNonExpired
) VALUES (
  :first_name,
  :last_name,
  :password,
  :panther_id,
  :email,
  :created_date,
  :isAccountNonExpired,
  :isAccountNonLocked,
  :isCredentialsNonExpired
);

--STATEMENT readUser
SELECT * FROM users WHERE id = :id;

--STATEMENT readAllUsers
SELECT * FROM users;

--STATEMENT readUserByEmail
SELECT * FROM users WHERE email = :email;

--STATEMENT deleteUser
DELETE FROM users WHERE id = :id;

--STATEMENT updateUser
UPDATE users SET
  first_name = :first_name,
  last_name = :last_name,
  password = :password,
  panther_id = :panther_id,
  email = :email,
  isAccountNonExpired = :isAccountNonExpired,
  isAccountNonLocked = :isAccountNonLocked,
  isCredentialsNonExpired = :isCredentialsNonExpired,
  updated_date = :updated_date
WHERE
  id = :id;

