-- =============================
--  SCHEMA SETUP
-- =============================

-- TEAMS TABLE
CREATE TABLE teams (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- USERS TABLE
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    userpassword VARCHAR(255) NOT NULL,
    team_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL
);

-- FILES TABLE
CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    filepath VARCHAR(500) NOT NULL,
    team_id INT,
    uploaded_by INT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE SET NULL
);

-- =============================
--  INSERT SAMPLE DATA
-- =============================

-- TEAMS
INSERT INTO teams (name) VALUES
    ('Developers'),
    ('Designers'),
    ('Marketing');

-- USERS
INSERT INTO users (username, email, userpassword, team_id) VALUES
    ('abdul', 'abdul@hotmail.com', 'pass123', 1),
    ('mary', 'mary@example.com', 'hello456', 2),
    ('alex', 'alex@example.com', 'dev789', 1);


