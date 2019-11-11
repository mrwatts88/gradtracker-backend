--STATEMENT createUserRole
INSERT INTO user_roles (
  user_id,
  role_id
) VALUES (
  :user_id,
  :role_id
);

--STATEMENT createUserRolesByRoleName
INSERT INTO user_roles (
  user_id,
  role_id
)  VALUES (
   :user_id,
   (SELECT roles.id FROM roles WHERE roles.name = :role_name)
)

--STATEMENT readUserRole
SELECT * FROM user_roles WHERE id = :id;

--STATEMENT readAllUserRole
SELECT * FROM user_roles;

--STATEMENT readUserRoleByRoleId
SELECT * FROM user_roles WHERE role_id = :role_id;

--STATEMENT deleteUserRoleByUserIdAndRoleName
DELETE FROM user_roles
WHERE role_id = (SELECT id FROM roles WHERE name = :role_name)
AND user_id = :user_id;

--STATEMENT deleteUserRolesByUserId
DELETE FROM user_roles WHERE user_id = :user_id;

--STATEMENT deleteUserRolesByRoleId
DELETE FROM user_roles WHERE role_id = :role_id;

