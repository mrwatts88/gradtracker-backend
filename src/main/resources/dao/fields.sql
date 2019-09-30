--STATEMENT createfield
INSERT INTO fields (
  form_id,
  label,
  field_data,
  data_type_flag,
  field_created_date
) VALUES (
  :form_id,
  :label,
  :field_data,
  :data_type_flag,
  :field_created_date
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