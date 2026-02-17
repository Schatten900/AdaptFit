-- V3: Adiciona tabelas para workouts
CREATE TABLE workouts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    user_id BIGINT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE workout_days (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    workout_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    day_of_week INT,
    day_order INT,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);

-- 3️⃣ exercise_catalog (ANTES)
CREATE TABLE exercise_catalog (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    muscle_group VARCHAR(100),
    equipment VARCHAR(100),
    is_bodyweight BOOLEAN DEFAULT FALSE,
    created_at DATETIME
);

-- 4️⃣ workout_exercises (DEPOIS)
CREATE TABLE workout_exercises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    workout_day_id BIGINT NOT NULL,
    exercise_id BIGINT NOT NULL,

    sets INT,
    reps INT,
    weight DOUBLE,
    rest_time_seconds INT,
    exercise_order INT,

    CONSTRAINT fk_workout_exercise_day
        FOREIGN KEY (workout_day_id)
        REFERENCES workout_days(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_workout_exercise_catalog
        FOREIGN KEY (exercise_id)
        REFERENCES exercise_catalog(id)
);

CREATE TABLE workout_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    workout_id BIGINT NOT NULL,
    session_date DATETIME NOT NULL,
    duration_minutes INT,
    notes VARCHAR(1000),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
);
