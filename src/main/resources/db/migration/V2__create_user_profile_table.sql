CREATE TABLE user_profile (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    address VARCHAR(255),
    phone_number VARCHAR(50),
    date_of_birth DATE,
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id)
);
