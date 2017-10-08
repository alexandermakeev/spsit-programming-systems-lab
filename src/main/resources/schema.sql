CREATE TABLE IF NOT EXISTS matrices (
  id BIGSERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS cells (
  id        BIGSERIAL PRIMARY KEY,
  row       INTEGER NOT NULL,
  col       INTEGER NOT NULL,
  value     REAL    NOT NULL,
  matrix_id BIGINT REFERENCES matrices (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS logs (
  id               BIGSERIAL PRIMARY KEY,
  remote_address   VARCHAR(45),
  created_at       TIMESTAMP,
  matrix_a_id      BIGINT REFERENCES matrices (id) ON DELETE CASCADE ON UPDATE CASCADE,
  matrix_b_id      BIGINT REFERENCES matrices (id) ON DELETE CASCADE ON UPDATE CASCADE,
  matrix_result_id BIGINT REFERENCES matrices (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE OR REPLACE VIEW logs_view AS
  SELECT
    id,
    remote_address,
    created_at
  FROM logs;