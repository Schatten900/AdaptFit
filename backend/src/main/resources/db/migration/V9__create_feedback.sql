-- Logs de evolução (ajustes de carga/calorias)
CREATE TABLE evolution_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    adjustment_reason VARCHAR(30),
    previous_value DOUBLE,
    new_value DOUBLE,
    parameter_name VARCHAR(50),
    description VARCHAR(500),
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Feedback pós-treino
CREATE TABLE feedbacks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    workout_session_id BIGINT,
    fatigue_level INT NOT NULL,
    muscle_soreness INT NOT NULL,
    notes VARCHAR(1000),
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (workout_session_id) REFERENCES workout_sessions(id) ON DELETE SET NULL
);


-- Decision logs da IA
CREATE TABLE ai_decision_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    session_id BIGINT,
    agent_type VARCHAR(50),
    decision VARCHAR(50),
    reason VARCHAR(1000),
    input_data JSON,
    confidence DOUBLE,
    created_at DATETIME
);