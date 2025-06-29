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

-- Insert roles for the new entity structure
INSERT INTO roles (active, description, role_name) VALUES (true, 'administrator roles', 'ROLE_ADMIN');
INSERT INTO roles (active, description, role_name) VALUES (true, 'user roles', 'ROLE_USER');

-- Insert users with the new entity structure
INSERT INTO users (password, user_name, are_credentials_expired, is_enabled, is_expired, is_locked) 
VALUES ('$2a$10$bJxwWc3A3DBzke7Gnb/MZ.lLXmvOIE/DFAd6QUnBvWhn7c7D1zY4C', 'Ruthger', false, true, false, false);

INSERT INTO users (password, user_name, are_credentials_expired, is_enabled, is_expired, is_locked) 
VALUES ('$2a$10$bJxwWc3A3DBzke7Gnb/MZ.lLXmvOIE/DFAd6QUnBvWhn7c7D1zY4C', 'Edwin', false, true, false, false);

-- Insert user-role relationships
INSERT INTO user_role (role_id, user_id) VALUES (1, 1);
INSERT INTO user_role (role_id, user_id) VALUES (2, 2);
INSERT INTO user_role (role_id, user_id) VALUES (2, 1);

