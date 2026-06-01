-- Populate worker table (at least 5 workers)
INSERT INTO worker (name, birthday, level, salary) VALUES
('John Doe', '1990-05-15', 'Senior', 5000),
('Jane Smith', '1995-10-20', 'Middle', 3500),
('Bob Johnson', '1998-03-12', 'Junior', 2000),
('Alice Brown', '2001-07-04', 'Trainee', 1000),
('Charlie Green', '1985-12-30', 'Senior', 8000),
('David White', '1992-04-25', 'Middle', 4000);

-- Populate client table (at least 5 clients)
INSERT INTO client (name) VALUES
('Acme Corp'),
('Beta Industries'),
('Global Tech'),
('Dynamic Solutions'),
('Apex Systems');

-- Populate project table (at least 5 projects)
INSERT INTO project (client_id, start_date, finish_date) VALUES
(1, '2023-01-01', '2023-06-01'), -- 5 months
(2, '2023-02-15', '2023-08-15'), -- 6 months
(3, '2022-05-01', '2023-05-01'), -- 12 months
(4, '2023-03-01', '2023-04-01'), -- 1 month
(5, '2023-01-10', '2024-01-10'), -- 12 months
(1, '2023-05-01', '2023-12-01'); -- 7 months

-- Populate project_worker table
INSERT INTO project_worker (project_id, worker_id) VALUES
(1, 1), (1, 2),
(2, 2), (2, 3),
(3, 1), (3, 5), (3, 6),
(4, 4),
(5, 5), (5, 6),
(6, 1), (6, 3);
