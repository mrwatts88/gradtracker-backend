--STATEMENT createRole
INSERT INTO roles (
  name,
  description,
  created_date
) VALUES (
  :name,
  :description,
  :created_date
);

--STATEMENT readRoleById
SELECT roles.*, role_authorities.authority
FROM roles
INNER JOIN role_authorities
    ON role_authorities.role_id = roles.id
WHERE roles.id = :id
ORDER BY role_authorities.authority

--STATEMENT readRoleByName
SELECT roles.*, role_authorities.authority
FROM roles
INNER JOIN role_authorities
    ON role_authorities.role_id = roles.id
WHERE roles.name = :name
ORDER BY role_authorities.authority

--STATEMENT readAllRoles
SELECT roles.*, role_authorities.authority
FROM roles
INNER JOIN role_authorities
    ON role_authorities.role_id = roles.id
ORDER BY roles.name, role_authorities.authority

--STATEMENT deleteRole
DELETE
FROM roles
INNER JOIN role_authorities
    ON role_authorities.role_id = roles.id
WHERE roles.id = :id OR role_authorities.role_id = :id;

--STATEMENT updateRole
UPDATE roles SET
  name = :name,
  description = :description,
  updated_date = :updated_date
WHERE
  id = :id;

