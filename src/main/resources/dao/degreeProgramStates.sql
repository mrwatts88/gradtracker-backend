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

--STATEMENT readAllDegreeProgramStatesWithDegreePrograms --todo: cool report functionality, but is it necessary?
SELECT dp.id
     , dp.name AS degree_program_name
     , dps.id AS degree_program_id
     , dps.name AS state_name
     , dps.description AS state_description
     , dps.created_date
     , dps.updated_date
FROM degree_program_states dps
INNER JOIN degree_programs dp
    ON dp.id = dps.degree_program_id
ORDER BY degree_program_name, state_name;

--STATEMENT readDegreeProgramStateById
SELECT *
FROM degree_program_states
WHERE id = :id;

--STATEMENT readDegreeProgramStatesByDegreeProgramId
SELECT *
FROM degree_program_states
WHERE degree_program_id = :degree_program_id
ORDER BY name;

--STATEMENT updateDegreeProgramStateById
UPDATE degree_program_states SET
    name = :name,
    description = :description,
    updated_date = :updated_date
WHERE id = :id;

--STATEMENT deleteDegreeProgramStateById
DELETE FROM degree_program_states
WHERE id = :id;



