--STATEMENT createForm
INSERT INTO forms (
  id,
  form_def_id,
  user_id,
  approved,
  created_date
) VALUES (
  :id,
  :form_def_id,
  :user_id,
  :approved,
  :created_date
);

--STATEMENT readForm
SELECT * FROM forms WHERE id = :id;

--STATEMENT readAllForm
SELECT * FROM forms;

--STATEMENT readAllFormsByUserId
SELECT * FROM forms WHERE user_id = :user_id;

--STATEMENT deleteForm
DELETE FROM forms WHERE id = :id;

--STATEMENT updateForm
UPDATE forms SET
  form_def_id = :form_def_id,
  user_id = :user_id,
  approved = :approved,
  updated_date = :updated_date
WHERE
  id = :id;