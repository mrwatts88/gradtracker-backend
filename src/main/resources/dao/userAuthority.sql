--STATEMENT createUserAuthority
INSERT INTO user_authorities (
  authority,
  role_id,
  created_date
) VALUES (
  :authority,
  :role_id,
  :created_date
);

--STATEMENT readUserAuthority
SELECT * FROM user_authorities WHERE id = :id;

--STATEMENT readAllUserAuthority
SELECT * FROM user_authorities;

--STATEMENT readUserAuthorityByRoleId
SELECT * FROM user_authorities WHERE role_id = :role_id;

--STATEMENT deleteUserAuthority
DELETE FROM user_authorities WHERE id = :id;

--STATEMENT updateUserAuthority
UPDATE user_authorities SET
  authority = :authority,
  role_id = :role_id,
  updated_date = :updated_date
WHERE
  id = :id;

