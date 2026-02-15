INSERT INTO exercise_catalog (name, description, muscle_group, equipment, is_bodyweight, created_at, created_by) VALUES
-- PEITO
('Supino Reto', 'Supino com barra em banco reto', 'Peito', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Supino Inclinado', 'Supino com barra em banco inclinado', 'Peito', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Supino Halter', 'Supino com halteres', 'Peito', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Crucifixo', 'Abertura com halteres', 'Peito', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Crossover', 'Crossover no cabo', 'Peito', 'Cabo', FALSE, NOW(), 'SYSTEM'),
('Flexão', 'Flexão de braço tradicional', 'Peito', 'Corpo', TRUE, NOW(), 'SYSTEM'),

-- COSTAS
('Puxada Frontal', 'Puxada na frente no pulley', 'Costas', 'Cabo', FALSE, NOW(), 'SYSTEM'),
('Barra Fixa', 'Barra fixa pronada', 'Costas', 'Corpo', TRUE, NOW(), 'SYSTEM'),
('Remada Curvada', 'Remada com barra curvada', 'Costas', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Remada Unilateral', 'Remada com halter unilateral', 'Costas', 'Halter', FALSE, NOW(), 'SYSTEM'),
('Pulldown', 'Pulldown no pulley', 'Costas', 'Cabo', FALSE, NOW(), 'SYSTEM'),

-- OMBRO
('Desenvolvimento', 'Desenvolvimento com barra', 'Ombro', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Elevação Lateral', 'Elevação lateral com halteres', 'Ombro', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Elevação Frontal', 'Elevação frontal com halteres', 'Ombro', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Arnold Press', 'Desenvolvimento Arnold', 'Ombro', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Face Pull', 'Face pull no cabo', 'Ombro', 'Cabo', FALSE, NOW(), 'SYSTEM'),

-- BÍCEPS
('Rosca Direta', 'Rosca direta com barra', 'Bíceps', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Rosca Alternada', 'Rosca alternada com halteres', 'Bíceps', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Rosca Martelo', 'Rosca martelo', 'Bíceps', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Rosca Scott', 'Rosca Scott', 'Bíceps', 'Máquina', FALSE, NOW(), 'SYSTEM'),

-- TRÍCEPS
('Tríceps Pulley', 'Tríceps no pulley', 'Tríceps', 'Cabo', FALSE, NOW(), 'SYSTEM'),
('Tríceps Testa', 'Tríceps testa com barra', 'Tríceps', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Mergulho', 'Mergulho em paralelas', 'Tríceps', 'Corpo', TRUE, NOW(), 'SYSTEM'),

-- PERNAS
('Agachamento Livre', 'Agachamento com barra', 'Pernas', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Leg Press', 'Leg press 45°', 'Pernas', 'Máquina', FALSE, NOW(), 'SYSTEM'),
('Cadeira Extensora', 'Extensão de joelhos', 'Pernas', 'Máquina', FALSE, NOW(), 'SYSTEM'),
('Mesa Flexora', 'Flexão de joelhos', 'Pernas', 'Máquina', FALSE, NOW(), 'SYSTEM'),
('Avanço', 'Avanço com halteres', 'Pernas', 'Halteres', FALSE, NOW(), 'SYSTEM'),
('Stiff', 'Stiff com barra', 'Posterior', 'Barra', FALSE, NOW(), 'SYSTEM'),

-- GLÚTEO
('Elevação Pélvica', 'Hip Thrust', 'Glúteo', 'Barra', FALSE, NOW(), 'SYSTEM'),
('Abdução', 'Abdução de quadril', 'Glúteo', 'Máquina', FALSE, NOW(), 'SYSTEM'),

-- CORE
('Abdominal Crunch', 'Crunch no solo', 'Abdômen', 'Corpo', TRUE, NOW(), 'SYSTEM'),
('Prancha', 'Prancha isométrica', 'Abdômen', 'Corpo', TRUE, NOW(), 'SYSTEM'),
('Elevação de Pernas', 'Elevação de pernas suspenso', 'Abdômen', 'Corpo', TRUE, NOW(), 'SYSTEM'),

-- PANTURRILHA
('Panturrilha em Pé', 'Elevação de panturrilha em pé', 'Panturrilha', 'Máquina', FALSE, NOW(), 'SYSTEM'),
('Panturrilha Sentado', 'Elevação de panturrilha sentado', 'Panturrilha', 'Máquina', FALSE, NOW(), 'SYSTEM');
