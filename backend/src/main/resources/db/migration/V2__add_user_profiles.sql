-- V2: Adiciona tabela UserProfiles
CREATE TABLE UserProfiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    age INT NOT NULL,
    height DOUBLE NOT NULL,
    weight DOUBLE NOT NULL,
    goal VARCHAR(50),
    experience VARCHAR(50),
    gender VARCHAR(20),
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);