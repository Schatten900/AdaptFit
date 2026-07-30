-- Índices


-- Workout_day
CREATE INDEX idx_workout_days_user_id ON workout_days(user_id);

-- Workout_exercises
CREATE INDEX idx_workout_exercises_workout_day_id ON workout_exercises(workout_day_id);
CREATE INDEX idx_workout_sessions_user_date ON workout_sessions(user_id, local_date);
CREATE INDEX idx_workout_sessions_user_day ON workout_sessions(user_id, workout_day_id, local_date);

-- Session_exercises
CREATE INDEX idx_session_exercises_user_date ON workout_session_exercises(user_id, session_date);
CREATE INDEX idx_session_exercises_exercise_date ON workout_session_exercises(exercise_id, session_date);

-- Plans
CREATE INDEX idx_workout_plans_user ON workout_plans(user_id);
CREATE INDEX idx_nutritional_plans_user_active ON nutritional_plans(user_id, is_active);

-- Split
CREATE INDEX idx_split_workout_days_split ON split_workout_days(split_id);
CREATE INDEX idx_splits_user ON splits(user_id);

-- Feedback
CREATE INDEX idx_feedbacks_user ON feedbacks(user_id);
CREATE INDEX idx_feedbacks_session ON feedbacks(workout_session_id);
CREATE INDEX idx_evolution_logs_user ON evolution_logs(user_id);

-- Receitas
CREATE INDEX idx_recipes_recipe_id ON recipes(recipe_id);