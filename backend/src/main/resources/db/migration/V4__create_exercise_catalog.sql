-- Representa todos os exercicios do nosso banco de dados
CREATE TABLE exercise_catalog (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    primary_muscle VARCHAR(100),
    secondary_muscles JSON,
    is_bodyweight BOOLEAN DEFAULT FALSE,
    created_at DATETIME
);