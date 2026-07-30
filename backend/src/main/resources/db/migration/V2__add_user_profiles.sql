-- V2: Adiciona tabela UserProfiles
CREATE TABLE user_profiles (

    -- Identificadores
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE,

    -- Dados do corpo
    age INT NOT NULL,
    height DOUBLE NOT NULL,
    weight DOUBLE NOT NULL,
    gender VARCHAR(20),

    -- Objetivos
    goal VARCHAR(50),           -- LOST FAT, GAIN MUSCLE, RESISTANCE
    experience VARCHAR(50),     -- BEGGINER, INTERMEDIARE, ADVANCED

    days_per_week INT,          -- disponibilidade por semana
    session_duration INT,       -- minutos

    -- Temporalidade
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);