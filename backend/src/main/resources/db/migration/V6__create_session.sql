-- Representa o treino finalizado pelo usuario
CREATE TABLE workout_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    workout_day_id BIGINT NOT NULL,
    session_date DATETIME NOT NULL,
    local_date DATE,
    duration_minutes INT,
    notes VARCHAR(1000),
    total_reps INT,
    total_weight DOUBLE,
    total_volume DOUBLE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (workout_day_id) REFERENCES workout_days(id) ON DELETE CASCADE
);

-- Representa os exercicios realizados no treino finalizado do usuario (Snapshot de workout_day)
CREATE TABLE workout_session_exercises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,
    workout_day_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    session_date DATE NOT NULL,
    set_number INT NOT NULL,
    reps INT,
    weight DOUBLE,
    volume DOUBLE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES workout_sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercise_catalog(id),
    FOREIGN KEY (workout_day_id) REFERENCES workout_days(id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);