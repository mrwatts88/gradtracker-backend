--STATEMENT createDegreeProgramState
INSERT INTO degree_program_states (
    id,
    degree_program_id,
    name,
    is_initial,
    description,
    created_date
) VALUES (
    :id,
    :degree_program_id,
    :name,
    :is_initial,
    :description,
    :created_date
)

--STATEMENT readAllDegreeProgramStates
SELECT *
FROM degree_program_states
ORDER BY name;

--STATEMENT readDegreeProgramStateById
SELECT *
FROM degree_program_states
WHERE id = :id;

--STATEMENT readDegreeProgramStatesByDegreeProgramId
SELECT *
FROM degree_program_states
WHERE degree_program_id = :degree_program_id;

--STATEMENT readDegreeProgramStatesIdsByDegreeProgramId
SELECT id
FROM degree_program_states
WHERE degree_program_id = :degree_program_id;

--STATEMENT updateDegreeProgramStateById
UPDATE degree_program_states SET
    name = :name,
    description = :description,
    updated_date = :updated_date
WHERE id = :id;

--STATEMENT deleteDegreeProgramStateById
DELETE FROM degree_program_states
WHERE id = :id;



