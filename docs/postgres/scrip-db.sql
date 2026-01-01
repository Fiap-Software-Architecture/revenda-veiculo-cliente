CREATE TABLE IF NOT EXISTS veiculos (
    id UUID PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE, 
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(150) NOT NULL,
    ano INTEGER NOT NULL,
    cor VARCHAR(50) NOT NULL,
    preco NUMERIC(15, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE
);


CREATE TABLE IF NOT EXISTS clientes (
    id UUID PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    nome VARCHAR(255),
    sobrenome VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS compras (
    cliente_id UUID NOT NULL,
    veiculo_id UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_compras PRIMARY KEY (cliente_id, veiculo_id)
);

CREATE INDEX idx_compras_cliente_id ON compras (cliente_id);
CREATE INDEX idx_compras_veiculo_id ON compras (veiculo_id);