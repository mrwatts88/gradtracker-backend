--STATEMENT createRoleAuthority
INSERT INTO role_authorities (
  authority,
  role_id
) VALUES (
  :authority,
  :role_id
);

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

--STATEMENT deleteRoleAuthorityByNameAndRoleId
DELETE FROM role_authorities WHERE authority = :authority AND role_id = :role_id;

--STATEMENT deleteRoleAuthoritiesByRoleId
DELETE FROM role_authorities WHERE role_id = :role_id;