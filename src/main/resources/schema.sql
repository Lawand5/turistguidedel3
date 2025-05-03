CREATE TABLE IF NOT EXISTS tourist_attraction (
                                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                                  name VARCHAR(255) NOT NULL,
    description TEXT,
    city VARCHAR(255),
    tags TEXT
    );
