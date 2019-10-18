--STATEMENT createForm
INSERT INTO forms (
  form_id,
  form_def_id,
  user_id,
  approved,
  created_date
) VALUES (
  :form_id,
  :form_def_id,
  :user_id,
  :approved,
  :created_date
);

--STATEMENT readForm
SELECT * FROM forms WHERE form_id = :form_id;

--STATEMENT readAllForm
SELECT * FROM forms;

--STATEMENT deleteForm
DELETE FROM forms WHERE form_id = :form_id;
