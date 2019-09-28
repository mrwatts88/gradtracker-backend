--STATEMENT createProfile
INSERT INTO profiles (
  first_name,
  last_name,
  panther_id,
  email,
  created_date
) VALUES (
  :first_name,
  :last_name,
  :panther_id,
  :email,
  :created_date
);

--STATEMENT readProfile
SELECT * FROM profiles WHERE id = :id;

--STATEMENT readProfileByNameAndProject
--SELECT * FROM profiles WHERE name = :name AND project = :project;

--STATEMENT deleteProfile
DELETE FROM profiles WHERE id = :id;

--STATEMENT updateProfile
UPDATE profiles SET
  first_name = :first_name,
  last_name = :last_name,
  panther_id = :panther_id,
  email = :email,
  updated_date = :updated_date
WHERE
  id = :id;

--STATEMENT getAllProfilesByProject
--SELECT * from profiles where project = :project;

