-- V3: Adiciona tabelas para workouts

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

-- exercise_catalog
CREATE TABLE exercise_catalog (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    muscle_group VARCHAR(100),
    equipment VARCHAR(100),
    is_bodyweight BOOLEAN DEFAULT FALSE,
    created_at DATETIME
);

-- workout_exercises (prescritos no workout day)
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

-- workout_sessions (sessão realizada pelo usuário)
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

-- workout_session_exercises (exercícios realizados por sessão - detalhado por série)
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

CREATE INDEX idx_workout_days_user_id ON workout_days(user_id);
CREATE INDEX idx_workout_exercises_workout_day_id ON workout_exercises(workout_day_id);
CREATE INDEX idx_workout_sessions_user_date ON workout_sessions(user_id, local_date);
CREATE INDEX idx_workout_sessions_user_day ON workout_sessions(user_id, workout_day_id, local_date);
CREATE INDEX idx_session_exercises_user_date ON workout_session_exercises(user_id, session_date);
CREATE INDEX idx_session_exercises_exercise_date ON workout_session_exercises(exercise_id, session_date);
