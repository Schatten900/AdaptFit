-- Flyway V1: inicialização do banco

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    premium BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NULL,
    updated_at DATETIME NULL
);
