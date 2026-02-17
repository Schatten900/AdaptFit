INSERT INTO exercise_catalog
(name, description, muscle_group, equipment, is_bodyweight, created_at)
VALUES
('Supino Reto', 'Supino com barra em banco reto', 'Peito', 'Barra', FALSE, NOW()),
('Supino Inclinado', 'Supino com barra em banco inclinado', 'Peito', 'Barra', FALSE, NOW()),
('Supino Halter', 'Supino com halteres', 'Peito', 'Halteres', FALSE, NOW()),
('Crucifixo', 'Abertura com halteres', 'Peito', 'Halteres', FALSE, NOW()),
('Crossover', 'Crossover no cabo', 'Peito', 'Cabo', FALSE, NOW()),
('Flexão', 'Flexão de braço tradicional', 'Peito', 'Corpo', TRUE, NOW()),

('Puxada Frontal', 'Puxada na frente no pulley', 'Costas', 'Cabo', FALSE, NOW()),
('Barra Fixa', 'Barra fixa pronada', 'Costas', 'Corpo', TRUE, NOW()),
('Remada Curvada', 'Remada com barra curvada', 'Costas', 'Barra', FALSE, NOW()),
('Remada Unilateral', 'Remada com halter unilateral', 'Costas', 'Halter', FALSE, NOW()),
('Pulldown', 'Pulldown no pulley', 'Costas', 'Cabo', FALSE, NOW()),

('Desenvolvimento', 'Desenvolvimento com barra', 'Ombro', 'Barra', FALSE, NOW()),
('Elevação Lateral', 'Elevação lateral com halteres', 'Ombro', 'Halteres', FALSE, NOW()),
('Elevação Frontal', 'Elevação frontal com halteres', 'Ombro', 'Halteres', FALSE, NOW()),
('Arnold Press', 'Desenvolvimento Arnold', 'Ombro', 'Halteres', FALSE, NOW()),
('Face Pull', 'Face pull no cabo', 'Ombro', 'Cabo', FALSE, NOW()),

('Rosca Direta', 'Rosca direta com barra', 'Bíceps', 'Barra', FALSE, NOW()),
('Rosca Alternada', 'Rosca alternada com halteres', 'Bíceps', 'Halteres', FALSE, NOW()),
('Rosca Martelo', 'Rosca martelo', 'Bíceps', 'Halteres', FALSE, NOW()),
('Rosca Scott', 'Rosca Scott', 'Bíceps', 'Máquina', FALSE, NOW()),

('Tríceps Pulley', 'Tríceps no pulley', 'Tríceps', 'Cabo', FALSE, NOW()),
('Tríceps Testa', 'Tríceps testa com barra', 'Tríceps', 'Barra', FALSE, NOW()),
('Mergulho', 'Mergulho em paralelas', 'Tríceps', 'Corpo', TRUE, NOW()),

('Agachamento Livre', 'Agachamento com barra', 'Pernas', 'Barra', FALSE, NOW()),
('Leg Press', 'Leg press 45°', 'Pernas', 'Máquina', FALSE, NOW()),
('Cadeira Extensora', 'Extensão de joelhos', 'Pernas', 'Máquina', FALSE, NOW()),
('Mesa Flexora', 'Flexão de joelhos', 'Pernas', 'Máquina', FALSE, NOW()),
('Avanço', 'Avanço com halteres', 'Pernas', 'Halteres', FALSE, NOW()),
('Stiff', 'Stiff com barra', 'Posterior', 'Barra', FALSE, NOW()),

('Elevação Pélvica', 'Hip Thrust', 'Glúteo', 'Barra', FALSE, NOW()),
('Abdução', 'Abdução de quadril', 'Glúteo', 'Máquina', FALSE, NOW()),

('Abdominal Crunch', 'Crunch no solo', 'Abdômen', 'Corpo', TRUE, NOW()),
('Prancha', 'Prancha isométrica', 'Abdômen', 'Corpo', TRUE, NOW()),
('Elevação de Pernas', 'Elevação de pernas suspenso', 'Abdômen', 'Corpo', TRUE, NOW()),

('Panturrilha em Pé', 'Elevação de panturrilha em pé', 'Panturrilha', 'Máquina', FALSE, NOW()),
('Panturrilha Sentado', 'Elevação de panturrilha sentado', 'Panturrilha', 'Máquina', FALSE, NOW());
