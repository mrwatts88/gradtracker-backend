--STATEMENT createUserAuthority
INSERT INTO role_authorities (
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

--STATEMENT readRoleAuthoritiesByRoleId
SELECT role_authorities.authority FROM role_authorities WHERE role_id = :role_id;

--STATEMENT readRoleAuthoritiesByUserId
SELECT roles.name, role_authorities.authority
FROM role_authorities
INNER JOIN roles
    ON roles.id = role_authorities.role_id
INNER JOIN user_roles
    ON user_roles.role_id = roles.id
WHERE user_roles.user_id = :user_id;

--STATEMENT deleteRoleAuthority
DELETE FROM role_authorities WHERE id = :id;

--STATEMENT deleteRoleAuthoritiesByRoleId
DELETE FROM role_authorities WHERE role_id = :role_id;

--STATEMENT updateRoleAuthority
UPDATE role_authorities SET
  authority = :authority,
  role_id = :role_id,
  updated_date = :updated_date
WHERE
  id = :id;