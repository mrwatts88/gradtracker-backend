--STATEMENT createform
INSERT INTO forms (
  form_id,
  user_id,
  form_created_date
) VALUES (
  :form_id,
  :user_id,
  :form_created_date
);

--STATEMENT readform
SELECT * FROM forms WHERE form_id = :form_id;

--STATEMENT deleteform
DELETE FROM forms WHERE form_id = :form_id;

--STATEMENT updateform
UPDATE forms SET
  user_id = :user_id,
  form_updated_date = :form_updated_date
WHERE
  form_id = :form_id;