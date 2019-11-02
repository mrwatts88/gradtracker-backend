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

--STATEMENT readRoleAuthority
SELECT * FROM role_authorities WHERE id = :id;

--STATEMENT readAllRoleAuthority
SELECT * FROM role_authorities;

--STATEMENT readRoleAuthorityByRoleId
SELECT * FROM role_authorities WHERE role_id = :role_id;

--STATEMENT deleteRoleAuthority
DELETE FROM role_authorities WHERE id = :id;

--STATEMENT updateRoleAuthority
UPDATE role_authorities SET
  authority = :authority,
  role_id = :role_id,
  updated_date = :updated_date
WHERE
  id = :id;