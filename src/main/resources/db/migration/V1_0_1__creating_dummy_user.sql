-- Create the users table
CREATE TABLE dummy_users (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Insert a sample user
INSERT INTO dummy_users (firstname, lastname, email)
VALUES ('John', 'Doe', 'john.doe@example.com');
