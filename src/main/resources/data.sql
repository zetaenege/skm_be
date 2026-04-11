TRUNCATE TABLE users, teams, tournaments RESTART IDENTITY CASCADE;

INSERT INTO tournaments (id, name, start_date, end_date,city)
VALUES (10, 'Torneo Apertura 2026',  '2026-01-31', '2026-12-31', 'Caracas');

INSERT INTO teams (id, name, city, tournament_id, points, matches, win, draw, lost) VALUES
                                                                                        (10, 'Team 1', 'Ciudad A', 10, 0, 0, 0, 0, 0),
                                                                                        (11, 'Team 2', 'Ciudad B', 10, 0, 0, 0, 0, 0),
                                                                                        (12, 'Team 3', 'Ciudad C', 10, 0, 0, 0, 0, 0);


INSERT INTO users (user_id, name, email, password, position, is_coach, is_admin, team_id, img_profile) VALUES
                                                                                                           (10, 'Carlos Delantero', 'carlos@team1.com', '$2a$12$R9h/lSAbv.55G.CDU6vPue0YlJ6D1vOEq3pLQ6yUscKSeBInXWv6.', 'Delantero', false, false, 10, 'https://api.dicebear.com/7.x/avataaars/svg?seed=Carlos'),
                                                                                                           (11, 'Juan Portero', 'juan@team1.com', '$2a$12$R9h/lSAbv.55G.CDU6vPue0YlJ6D1vOEq3pLQ6yUscKSeBInXWv6.', 'Portero', false, false, 10, 'https://api.dicebear.com/7.x/avataaars/svg?seed=Juan'),


                                                                                                           (12, 'Luis Medio', 'luis@team2.com', '$2a$12$R9h/lSAbv.55G.CDU6vPue0YlJ6D1vOEq3pLQ6yUscKSeBInXWv6.', 'Medio', false, false, 11, 'https://api.dicebear.com/7.x/avataaars/svg?seed=Luis'),
                                                                                                           (13, 'Pedro Defensa', 'pedro@team2.com', '$2a$12$R9h/lSAbv.55G.CDU6vPue0YlJ6D1vOEq3pLQ6yUscKSeBInXWv6.', 'Defensa', false, false, 11, 'https://api.dicebear.com/7.x/avataaars/svg?seed=Pedro'),


                                                                                                           (14, 'Sergio Atacante', 'sergio@team3.com', '$2a$12$R9h/lSAbv.55G.CDU6vPue0YlJ6D1vOEq3pLQ6yUscKSeBInXWv6.', 'Delantero', false, false, 12, 'https://api.dicebear.com/7.x/avataaars/svg?seed=Sergio'),
                                                                                                           (15, 'Andres Muro', 'andres@team3.com', '$2a$12$R9h/lSAbv.55G.CDU6vPue0YlJ6D1vOEq3pLQ6yUscKSeBInXWv6.', 'Defensa', false, false, 12, 'https://api.dicebear.com/7.x/avataaars/svg?seed=Andres');


SELECT setval('users_user_id_seq', 30);
SELECT setval('teams_id_seq', 30);
SELECT setval('tournaments_id_seq', 30);
