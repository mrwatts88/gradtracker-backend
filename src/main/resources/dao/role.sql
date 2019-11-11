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
SELECT * from roles WHERE id = :id

--STATEMENT readRoleByName
SELECT * from roles WHERE name = :role_name

--STATEMENT readAllRoles
SELECT *FROM roles;

--STATEMENT deleteRole
DELETE FROM roles WHERE id = :id;

--STATEMENT updateRole
UPDATE roles SET
  name = :name,
  description = :description,
  updated_date = :updated_date
WHERE
  id = :id;

