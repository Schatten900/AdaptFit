-- V2: Adiciona tabela UserProfiles
CREATE TABLE UserProfiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    age INT NOT NULL,
    height DOUBLE NOT NULL,
    weight DOUBLE NOT NULL,
    goal VARCHAR(50),
    experience VARCHAR(50),
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);