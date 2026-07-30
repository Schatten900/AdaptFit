-- V3: Adiciona tabelas para workouts

-- Representa o treino personalizado criado pelo usuario
CREATE TABLE workout_days (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    day_of_week INT,
    day_order INT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Entidade intermediaria que liga os exercicios do banco ao treino criado pelo usuario
CREATE TABLE workout_exercises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    workout_day_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    sets INT,
    reps INT,
    weight DOUBLE,
    rest_time_seconds INT,
    exercise_order INT,
    FOREIGN KEY (workout_day_id) REFERENCES workout_days(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercise_catalog(id)
);



