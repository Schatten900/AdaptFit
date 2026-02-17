-- V6: Recria workout_days com estrutura correta (após refatoração)
-- Esta migração corrige a estrutura para suportar WorkoutDays independentes

-- Remove a tabela existente e recria com estrutura correta
DROP TABLE IF EXISTS workout_exercises;
DROP TABLE IF EXISTS workout_days;

-- Recria workout_days com a estrutura correta
CREATE TABLE workout_days (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    workout_id BIGINT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    day_of_week INT,
    day_order INT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

-- Recria workout_exercises
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

-- Adiciona índices para melhorar performance
CREATE INDEX idx_workout_days_user_id ON workout_days(user_id);
CREATE INDEX idx_workout_days_workout_id ON workout_days(workout_id);
CREATE INDEX idx_workout_exercises_workout_day_id ON workout_exercises(workout_day_id);
