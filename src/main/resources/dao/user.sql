--STATEMENT createUser
INSERT INTO users (
  first_name,
  last_name,
  password,
  panther_id,
  email,
  enabled,
  current_state_id,
  is_account_non_expired,
  is_account_non_locked,
  is_credentials_non_expired,
  created_date
) VALUES (
  :first_name,
  :last_name,
  :password,
  :panther_id,
  :email,
  :enabled,
  :current_state_id,
  :is_account_non_expired,
  :is_account_non_locked,
  :is_credentials_non_expired,
  :created_date
);

--STATEMENT readUser
SELECT * FROM users WHERE id = :id;

--STATEMENT readAllUsers
SELECT * FROM users;

--STATEMENT readUserByEmail
SELECT * FROM users WHERE email = :email;

--STATEMENT readUserByPantherId
SELECT * FROM users WHERE panther_id = :panther_id;

--STATEMENT deleteUser
DELETE FROM users WHERE id = :id;

--STATEMENT updateUser
UPDATE users SET
  first_name = :first_name,
  last_name = :last_name,
  password = :password,
  panther_id = :panther_id,
  email = :email,
  enabled = :enabled,
  is_account_non_expired = :is_account_non_expired,
  is_account_non_locked = :is_account_non_locked,
  is_credentials_non_expired = :is_credentials_non_expired,
  updated_date = :updated_date
WHERE
  id = :id;

