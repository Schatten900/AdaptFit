-- Receitas

-- Nutritional plans (motor determinístico)
CREATE TABLE nutritional_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    tmb DOUBLE NOT NULL,
    tdee DOUBLE NOT NULL,
    target_calories DOUBLE NOT NULL,
    protein_grams DOUBLE NOT NULL,
    carbs_grams DOUBLE NOT NULL,
    fat_grams DOUBLE NOT NULL,
    goal VARCHAR(20),
    weight_kg DECIMAL(5,2),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE recipes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_id VARCHAR(255) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    modo_preparo VARCHAR(2000),
    calorias_por_porcao INT,
    proteina_g DOUBLE,
    carboidratos_g DOUBLE,
    gorduras_g DOUBLE,
    dificuldade VARCHAR(50),
    tempo_minuto INT,
    created_at DATETIME
);

-- Collection tables da receita
CREATE TABLE recipe_ingredientes (
    recipe_id BIGINT NOT NULL,
    ingrediente VARCHAR(500),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE recipe_tipos_refeicao (
    recipe_id BIGINT NOT NULL,
    tipo_refeicao VARCHAR(30),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE recipe_dietas (
    recipe_id BIGINT NOT NULL,
    dieta VARCHAR(30),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE recipe_alergenios (
    recipe_id BIGINT NOT NULL,
    alergenio VARCHAR(255),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE TABLE recipe_objetivos (
    recipe_id BIGINT NOT NULL,
    objetivo VARCHAR(20),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);