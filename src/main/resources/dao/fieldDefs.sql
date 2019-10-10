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

--STATEMENT readfield
SELECT * FROM fields WHERE field_id = :field_id;

--STATEMENT deletefield
DELETE FROM fields WHERE field_id = :field_id;

--STATEMENT updatefield
UPDATE fields SET
  form_id = :form_id,
  label = :label,
  field_data = :field_data,
  data_type_flag = :data_type_flag,
  field_updated_date = :field_updated_date
WHERE
  field_id = :field_id;