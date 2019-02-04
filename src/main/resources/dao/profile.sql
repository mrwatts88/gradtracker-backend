--STATEMENT createProfile
INSERT INTO profiles (
  name,
  project,
  created_date
) VALUES (
  :name,
  :project,
  :created_date
);

--STATEMENT readProfile
SELECT * FROM profiles WHERE id = :id;

--STATEMENT readProfileByNameAndProject
SELECT * FROM profiles WHERE name = :name AND project = :project;

--STATEMENT deleteProfile
DELETE FROM profiles WHERE id = :id;

--STATEMENT updateProfile
UPDATE profiles SET
  name = :name,
  project = :project,
  updated_date = :updated_date
WHERE
  id = :id;

--STATEMENT getAllProfilesByProject
SELECT * from profiles where project = :project;

