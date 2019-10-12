--STATEMENT createFormDef
INSERT INTO form_defs (
  id,
  created_date
) VALUES (
  :id,
  :created_date
);

--STATEMENT readFormDef
SELECT * FROM form_defs WHERE id = :id;

--STATEMENT readAllFormDefs
SELECT * FROM form_defs;

--STATEMENT deleteFormDef
DELETE FROM form_defs WHERE id = :id;

--STATEMENT updateFormDef
UPDATE form_defs SET
  updated_date = :updated_date
WHERE
  id = :id;