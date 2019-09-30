--STATEMENT createProfile
INSERT INTO profiles (
  first_name,
  last_name,
  password,
  panther_id,
  email,
  created_date
) VALUES (
  :first_name,
  :last_name,
  :password,
  :panther_id,
  :email,
  :created_date
);

--STATEMENT readProfile
SELECT * FROM profiles WHERE id = :id;

--STATEMENT readProfileByEmail
SELECT * FROM profiles WHERE email = :email;

--STATEMENT deleteProfile
DELETE FROM profiles WHERE id = :id;

--STATEMENT updateProfile
UPDATE profiles SET
  first_name = :first_name,
  last_name = :last_name,
  password = :password,
  panther_id = :panther_id,
  email = :email,
  updated_date = :updated_date
WHERE
  id = :id;

