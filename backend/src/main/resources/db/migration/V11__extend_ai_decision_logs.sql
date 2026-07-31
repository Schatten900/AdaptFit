ALTER TABLE ai_decision_logs
    ADD COLUMN prompt_sent TEXT AFTER confidence,
    ADD COLUMN response_received TEXT AFTER prompt_sent,
    ADD COLUMN model_used VARCHAR(50) AFTER response_received,
    ADD COLUMN temperature DOUBLE AFTER model_used,
    ADD COLUMN latency_ms BIGINT AFTER temperature,
    ADD COLUMN tokens_in INT AFTER latency_ms,
    ADD COLUMN tokens_out INT AFTER tokens_in,
    ADD COLUMN documents_retrieved JSON AFTER tokens_out;
