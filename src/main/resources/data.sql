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
    ('Rio Grande Games', 'United States', '1998', false);

-- Insert roles for the new entity structure
INSERT INTO roles (active, description, role_name) VALUES (true, 'administrator roles', 'ROLE_ADMIN');
INSERT INTO roles (active, description, role_name) VALUES (true, 'user roles', 'ROLE_USER');
INSERT INTO roles (active, description, role_name) VALUES (true, 'employee roles', 'ROLE_EMPLOYEE');

-- Insert users with bruikbare wachtwoorden (password: "password123")
INSERT INTO users (password, user_name, are_credentials_expired, is_enabled, is_expired, is_locked) 
VALUES ('$2a$10$bJxwWc3A3DBzke7Gnb/MZ.lLXmvOIE/DFAd6QUnBvWhn7c7D1zY4C', 'admin', false, true, false, false);

INSERT INTO users (password, user_name, are_credentials_expired, is_enabled, is_expired, is_locked) 
VALUES ('$2a$10$bJxwWc3A3DBzke7Gnb/MZ.lLXmvOIE/DFAd6QUnBvWhn7c7D1zY4C', 'user', false, true, false, false);

INSERT INTO users (password, user_name, are_credentials_expired, is_enabled, is_expired, is_locked) 
VALUES ('$2a$10$bJxwWc3A3DBzke7Gnb/MZ.lLXmvOIE/DFAd6QUnBvWhn7c7D1zY4C', 'employee', false, true, false, false);

-- Insert user-role relationships
INSERT INTO user_role (role_id, user_id) VALUES (1, 1); -- admin krijgt ROLE_ADMIN
INSERT INTO user_role (role_id, user_id) VALUES (2, 2); -- user krijgt ROLE_USER
INSERT INTO user_role (role_id, user_id) VALUES (3, 3); -- employee krijgt ROLE_EMPLOYEE
INSERT INTO user_role (role_id, user_id) VALUES (2, 1); -- admin krijgt ook ROLE_USER

-- Insert boardgames met uitgebreide testdata
INSERT INTO boardgames (name, price, min_players, max_players, genre, available, publisher_id) VALUES
('Catan', 39.99, 3, 4, 'Strategy', true, 1),
('Ticket to Ride', 49.99, 2, 5, 'Family', true, 3),
('Pandemic', 44.99, 2, 4, 'Cooperative', true, 2),
('Monopoly', 29.99, 2, 8, 'Family', false, 10),
('Chess', 19.99, 2, 2, 'Strategy', true, 9),
('Codenames', 24.99, 2, 8, 'Party', true, 2),
('Star Wars: Rebellion', 89.99, 2, 4, 'Strategy', true, 1),
('The Settlers of Catan', 42.99, 3, 4, 'Strategy', true, 1),
('Terraforming Mars', 59.99, 1, 5, 'Strategy', true, 5),
('Gloomhaven', 89.99, 1, 4, 'Adventure', true, 6),
('7 Wonders Duel', 39.99, 2, 2, 'Strategy', true, 2),
('Carcassonne', 29.99, 2, 5, 'Strategy', true, 14),
('Risk', 35.99, 2, 6, 'Strategy', true, 10),
('Scrabble', 25.99, 2, 4, 'Word', true, 10),
('Clue', 22.99, 3, 6, 'Mystery', true, 10),
('Battleship', 18.99, 2, 2, 'Strategy', true, 10),
('Connect Four', 12.99, 2, 2, 'Abstract', true, 10),
('Jenga', 15.99, 1, 8, 'Dexterity', true, 10),
('Uno', 8.99, 2, 10, 'Card', true, 10),
('Twister', 19.99, 2, 6, 'Party', true, 10);

