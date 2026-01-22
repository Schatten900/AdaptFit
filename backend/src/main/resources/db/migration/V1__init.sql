-- Flyway V1: inicialização do banco

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    premium BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE UserProfiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    age INT NOT NULL,
    height DOUBLE NOT NULL,
    weight DOUBLE NOT NULL,
    goal VARCHAR(50),
    experience VARCHAR(50),
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);