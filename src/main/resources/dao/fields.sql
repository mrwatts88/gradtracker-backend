--STATEMENT createField
INSERT INTO fields (
  id,
  field_def_id,
  form_id,
  data,
  created_date
) VALUES (
  :id,
  :field_def_id,
  :form_id,
  :data,
  :created_date
);

--STATEMENT readField
SELECT * FROM fields WHERE id = :id;

--STATEMENT readFieldsByFormId
SELECT * FROM fields WHERE form_id = :form_id;

--STATEMENT deleteField
DELETE FROM fields WHERE id = :id;

--STATEMENT deleteFieldsByFormId
DELETE FROM fields WHERE form_id = :form_id;

--STATEMENT updateField
UPDATE fields SET
  field_def_id = :field_def_id,
  form_id = :form_id,
  data = :data,
  updated_date = :updated_date
WHERE
  id = :id;
