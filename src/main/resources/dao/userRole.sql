--STATEMENT createUserRole
INSERT INTO user_roles (
  user_id,
  role_id,
  created_date
) VALUES (
  :user_id,
  :role_id,
  :created_date
);

--STATEMENT createUserRolesByRoleName
INSERT INTO user_roles (
  user_id,
  role_id,
  created_date
)  VALUES (
   :user_id,
   (SELECT roles.id FROM roles WHERE roles.name = :role_name),
   :created_date
)

--STATEMENT readUserRole
SELECT * FROM user_roles WHERE id = :id;

--STATEMENT readAllUserRole
SELECT * FROM user_roles;

--STATEMENT readUserRoleByRoleId
SELECT * FROM user_roles WHERE role_id = :role_id;

--STATEMENT deleteUserRole
DELETE FROM user_roles WHERE id = :id;

--STATEMENT updateUserRole
UPDATE user_roles SET
  user_id = :user_id,
  role_id = :role_id,
  updated_date = :updated_date
WHERE
  id = :id;

