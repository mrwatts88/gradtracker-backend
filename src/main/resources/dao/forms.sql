--STATEMENT createForm
INSERT INTO forms (
  form_def_id,
  user_id,
  approved,
  created_date
) VALUES (
  :form_def_id,
  :user_id,
  :approved,
  :created_date
);

--STATEMENT readForm
SELECT forms.*, form_defs.name
FROM forms
LEFT JOIN form_defs ON forms.form_def_id = form_defs.id
WHERE forms.id = :id;

--STATEMENT readAllForms
SELECT forms.*, form_defs.name
FROM forms
LEFT JOIN form_defs ON forms.form_def_id = form_defs.id;

--STATEMENT readAllFormsByUserId
SELECT forms.*, form_defs.name
FROM forms
LEFT JOIN form_defs ON forms.form_def_id = form_defs.id
WHERE forms.user_id = :user_id;

--STATEMENT readAllFormsByFormDefId
SELECT forms.*, form_defs.name
FROM forms
LEFT JOIN form_defs ON forms.form_def_id = form_defs.id
WHERE forms.form_def_id = :form_def_id;

--STATEMENT readAllFormsByPantherId
SELECT forms.*, form_defs.name
FROM forms
LEFT JOIN form_defs ON form_defs.id = forms.form_def_id
LEFT JOIN users ON users.id = forms.user_id
WHERE users.panther_id = :panther_id;

--STATEMENT deleteForm
DELETE FROM forms WHERE id = :id;

--STATEMENT updateForm
UPDATE forms SET
  form_def_id = :form_def_id,
  user_id = :user_id,
  approved = :approved,
  updated_date = :updated_date
WHERE
  id = :id;

--STATEMENT updateApproval
UPDATE forms SET
    approved = :approved
WHERE
    id = :id;