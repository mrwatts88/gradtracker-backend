--STATEMENT createDegreeProgram
INSERT INTO degree_programs (
    id,
    name,
    description,
    created_date,
) VALUES (
    :id,
    :name,
    :description,
    :created_date
);

--STATEMENT readAllDegreePrograms
SELECT *
FROM degree_programs
ORDER BY name;

--STATEMENT readDegreeProgramById
SELECT *
FROM degree_programs
WHERE id = :id;

--STATEMENT readDegreeProgramByName
SELECT *
FROM degree_programs
WHERE name = :name;

--STATEMENT updateDegreeProgramById
UPDATE degree_programs SET
    name = :name,
    description = :description,
    updated_date = :updated_date
WHERE id = :id;

--STATEMENT deleteDegreeProgramById
DELETE FROM degree_programs --TODO: add logic to handle delete in degree_program_states, users, and possibly transitions
WHERE id = :id;