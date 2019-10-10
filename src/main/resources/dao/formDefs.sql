--STATEMENT createFormDef
INSERT INTO form_defs (
  id,
  created_date
) VALUES (
  :id,
  :created_date
);

--STATEMENT readForm
SELECT * FROM forms WHERE form_id = :form_id;

--STATEMENT deleteForm
DELETE FROM forms WHERE form_id = :form_id;

--STATEMENT updateForm
UPDATE forms SET
  user_id = :user_id,
  form_updated_date = :form_updated_date
WHERE
  form_id = :form_id;