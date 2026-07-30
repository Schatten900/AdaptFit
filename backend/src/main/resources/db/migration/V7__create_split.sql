-- V8: Cria tabelas de entidades que não possuíam migration

-- Workout plans (motor determinístico)
CREATE TABLE workout_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    workout_split VARCHAR(20),
    experience_level VARCHAR(20),
    days_per_week INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    volume_multiplier DOUBLE,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Splits de treino
CREATE TABLE splits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Relação split ↔ workout_day
CREATE TABLE split_workout_days (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    split_id BIGINT NOT NULL,
    workout_day_id BIGINT NOT NULL,
    day_of_week INT NOT NULL,
    day_order INT NOT NULL,
    FOREIGN KEY (split_id) REFERENCES splits(id) ON DELETE CASCADE,
    FOREIGN KEY (workout_day_id) REFERENCES workout_days(id) ON DELETE CASCADE
);

