SELECT c.name, COUNT(p.id) AS project_count
FROM client c
JOIN project p ON c.id = p.client_id
GROUP BY c.id, c.name
HAVING COUNT(p.id) = (
    SELECT MAX(proj_cnt)
    FROM (
        SELECT COUNT(id) AS proj_cnt
        FROM project
        GROUP BY client_id
    )
);
