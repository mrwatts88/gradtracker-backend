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

--STATEMENT readRole
SELECT * FROM roles WHERE id = :id;

--STATEMENT readAllRole
SELECT * FROM roles;

--STATEMENT readRoleByName
SELECT * FROM roles WHERE name = :name;

--STATEMENT deleteRole
DELETE FROM roles WHERE id = :id;

--STATEMENT updateRole
UPDATE roles SET
  name = :name,
  description = :description,
  updated_date = :updated_date
WHERE
  id = :id;

