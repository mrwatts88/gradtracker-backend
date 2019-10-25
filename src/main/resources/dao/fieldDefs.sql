--STATEMENT createFieldDef
INSERT INTO field_defs (
  id,
  form_def_id,
  label,
  field_index,
  input_type,
  data_type,
  created_date
) VALUES (
  :id,
  :form_def_id,
  :label,
  :field_index,
  :input_type,
  :data_type,
  :created_date
);

--STATEMENT readFieldDef
SELECT * FROM field_defs WHERE id = :id;

--STATEMENT readFieldDefsByFormDefId
SELECT * FROM field_defs WHERE form_def_id = :id;

--STATEMENT readFieldDefIdsByFormDefId
SELECT id FROM field_defs WHERE form_def_id = :id;

--STATEMENT deleteFieldDef
DELETE FROM field_defs WHERE id = :id;

--STATEMENT deleteFieldDefsByFormDefId
DELETE FROM field_defs WHERE form_def_id = :id;

--STATEMENT updateFieldDef
UPDATE field_defs SET
  label = :label,
  field_index = :field_index,
  input_type = :input_type,
  data_type = :data_type,
  updated_date = :updated_date
WHERE
  id = :id;