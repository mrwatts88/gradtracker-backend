--STATEMENT createFieldDef
INSERT INTO field_defs (
  id,
  form_def_id,
  label,
  data_type,
  created_date
) VALUES (
  :id,
  :form_def_id,
  :label,
  :data_type,
  :created_date
);

--STATEMENT readField
SELECT * FROM fields WHERE id = :id;

--STATEMENT deleteField
DELETE FROM fields WHERE id = :id;

--STATEMENT updateField
UPDATE fields SET
  form_def_id = :form_def_id,
  label = :label,
  data_type = :data_type,
  updated_date = :updated_date
WHERE
  id = :id;