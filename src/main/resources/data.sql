INSERT INTO publisher (name, country_of_origin, founded, is_indie)
VALUES
    ('Fantasy Flight Games', 'United States', '1995', false),
    ('Asmodee', 'France', '1995', false),
    ('Days of Wonder', 'United States', '2002', false),
    ('42 Games' , 'United States', '2018', true),
    ('Stonemaier Games', 'United States', '2012', true),
    ('Cephalofair Games', 'United States', '2015', true),
    ('Stronghold Games', 'United States', '2009', true),
    ('Gale Force Nine', 'United States', '1998', true),
    ('Avalon Hill', 'United States', '1952', false),
    ('Hasbro', 'United States', '1923', false),
    ('Mattel', 'United States', '1945', false),
    ('Winning Moves', 'United States', '1995', false),
    ('Milton Bradley', 'United States', '1860', false),
    ('Parker Brothers', 'United States', '1883', false),
    ('Ravensburger', 'Germany', '1883', false),
    ('Schmidt Spiele', 'Germany', '1907', false),
    ('HABA', 'Germany', '1938', false),
    ('Kosmos', 'Germany', '1822', false),
    ('Queen Games', 'Germany', '1992', false),
    ('999 Games', 'Netherlands', '1990', false),
    ('White Goblin Games', 'Netherlands', '2011', true),
    ('Goliath Games', 'Netherlands', '1980', false),
    ('Identity Games', 'Netherlands', '1992', false),
    ('Jumbo', 'Netherlands', '1853', false),
    ('Tactic', 'Finland', '1967', false),
    ('Lautapelit.fi', 'Finland', '2004', true),
    ('Playmore Games', 'Finland', '2016', true),
    ('Korea Boardgames', 'South Korea', '2005', true),
    ('Hobby World', 'Russia', '2001', true),
    ('Zvezda', 'Russia', '1990', true),
    ('Rightgames', 'Russia', '2000', true),
    ('G3', 'Russia', '2010', true),
    ('Hobby Japan', 'Japan', '1969', false),
    ('Arclight', 'Japan', '2002', true),
    ('Oink Games', 'Japan', '2010', true),
    ('Japon Brand', 'Japan', '2006', true),
    ('Gemblo', 'Japan', '2006', true),
    ('Hans im Glück', 'Germany', '1983', false),
    ('Kosmos', 'Germany', '1822', false),
    ('Rio Grande Games', 'United States', '1998', false);

-- INSERT INTO boardgames (name, price, minPlayers, maxPlayers, genre, available, publisher)
-- VALUES
--     ('Mansions of Madness', 99.99, 1, 5, 'Horror', true, (SELECT id FROM publisher WHERE name = 'Fantasy Flight Games')),
--     ('Betrayal at House on the Hill', 49.99, 3, 6, 'Horror', true, (SELECT id FROM publisher WHERE name = 'Avalon Hill'));

    -- Voeg andere spellen hier toe
INSERT INTO users (username, password, role) 
VALUES
    ('Ruthger', '$2a$12$IOfGkBw8cRrqz3NamrHG4O9FTXsPQ8Bo2/lNutMcitWIBzn1.Chj2', 'ADMIN'),
    ('Jeroen', '$2a$12$Ap0HvB1RO19eSOD7DtBtheFrbkVdxa0haEhz30W2lOGbFVyaubsmO', 'USER'),
    ('Margriet', '$2a$12$TnvQDqkxq0dWEdeCz7WubuM/qnvrxSzDBS0XmNnLita6rSrQvcAwC', 'USER'),
    ('Jip', '$2a$12$PocIYxXiNXSEX7Ggq0kynOF4ikO5e5oGaw4j/9tJP2hJx9Qlsw9oe', 'USER'),
    ('Janneke', '$2a$12$UTkb3xb9AUGLUeCgnww6F.HWvsFZcklVMeOv7TRMjwpG/uWxFmJ6.', 'ADMIN');

-- INSERT INTO reservations (customer_id, boardgame_id, reservation_date, participant_count, notes) 
-- VALUES
--     ((SELECT id FROM users WHERE username = 'Ruthger'), (SELECT id FROM boardgames WHERE name = 'Mansions of Madness'), '2025-06-01', 5, 'Game night!');
--     -- ((SELECT id FROM users WHERE username = 'Jeroen'), (SELECT id FROM boardgames WHERE name = 'Mansions of Madness'), '2025-06-01', 5, 'Game night!'),
--     -- ((SELECT id FROM users WHERE username = 'Margriet'), (SELECT id FROM boardgames WHERE name = 'Mansions of Madness'), '2025-06-01', 5, 'Game night!'),
--     -- ((SELECT id FROM users WHERE username = 'Jip'), (SELECT id FROM boardgames WHERE name = 'Mansions of Madness'), '2025-06-01', 5, 'Game night!'),
--     -- ((SELECT id FROM users WHERE username = 'Janneke'), (SELECT id FROM boardgames WHERE name = 'Mansions of Madness'), '2025-06-01', 5, 'Game night!'),
--     -- ((SELECT id FROM users WHERE username = 'Ruthger'), (SELECT id FROM boardgames WHERE name = 'Who Goes There'), '2025-03-13', 3, 'Payback!'),
--     -- ((SELECT id FROM users WHERE username = 'Janneke'), (SELECT id FROM boardgames WHERE name = 'Who Goes There'), '2025-03-13', 3, 'Payback!'), 
--     -- ((SELECT id FROM users WHERE username = 'Jip'), (SELECT id FROM boardgames WHERE name = 'Who Goes There'), '2025-03-13', 3, 'Payback!');
