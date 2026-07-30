-- Preferencias e restricoes do usuario

CREATE TABLE user_preferences (

    user_id BIGINT PRIMARY KEY,     -- Relacao 1:1 com usuario
    available_equipment JSON,       -- barbell, bench, cable...

    injuries JSON,                  -- lumbar_pain, knee_pain

    exercise_blacklist JSON,        -- deadlift

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);