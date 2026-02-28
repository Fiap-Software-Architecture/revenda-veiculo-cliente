CREATE TABLE IF NOT EXISTS clientes (
    id UUID PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    nome VARCHAR(255),
    sobrenome VARCHAR(255)
);

CREATE INDEX idx_compras_cliente_id ON compras (cliente_id);