--STATEMENT createField
INSERT INTO fields (
  field_id,
  field_def_id,
  form_id,
  data,
  created_date
) VALUES (
  :field_id,
  :field_def_id,
  :form_id,
  :data,
  :created_date
);

--STATEMENT readField
SELECT * FROM fields WHERE field_id = :field_id;

--STATEMENT readFieldsByFormId
SELECT * FROM fields WHERE form_id = :form_id;

--STATEMENT deleteField
DELETE FROM fields WHERE field_id = :field_id;

--STATEMENT deleteFieldsByFormId
DELETE FROM fields WHERE form_id = :form_id;

--STATEMENT updateField
UPDATE fields SET
  field_def_id = :field_def_id,
  form_id = :form_id,
  data = :data,
  updated_date = :updated_date
WHERE
  field_id = :field_id;
