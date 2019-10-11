--STATEMENT createFormDef
INSERT INTO form_defs (
  id,
  created_date
) VALUES (
  :id,
  :created_date
);

--STATEMENT readForm
SELECT * FROM forms WHERE id = :id;

--STATEMENT deleteForm
DELETE FROM forms WHERE id = :id;

--STATEMENT updateForm
UPDATE forms SET
  form_updated_date = :form_updated_date
WHERE
  id = :id;