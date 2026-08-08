CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(30) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    whatsapp VARCHAR(20),
    email VARCHAR(150),
    instagram VARCHAR(100),
    data_nascimento DATE,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE produto (
    id BIGSERIAL PRIMARY KEY,
    categoria_id BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    destaque BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria (id)
);

CREATE TABLE produto_imagem (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    principal BOOLEAN NOT NULL DEFAULT FALSE,
    ordem INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produto_imagem_produto
        FOREIGN KEY (produto_id)
        REFERENCES produto (id)
);

CREATE TABLE produto_variacao (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    diametro_cm NUMERIC(5, 2),
    quantidade_fatias INTEGER,
    quantidade_unidades INTEGER,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ordem INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produto_variacao_produto
        FOREIGN KEY (produto_id)
        REFERENCES produto (id),

    CONSTRAINT ck_produto_variacao_preco
        CHECK (preco >= 0),

    CONSTRAINT ck_produto_variacao_diametro
        CHECK (diametro_cm IS NULL OR diametro_cm > 0),

    CONSTRAINT ck_produto_variacao_fatias
        CHECK (quantidade_fatias IS NULL OR quantidade_fatias > 0),

    CONSTRAINT ck_produto_variacao_unidades
        CHECK (quantidade_unidades IS NULL OR quantidade_unidades > 0),

    CONSTRAINT uk_produto_variacao_nome
        UNIQUE (produto_id, nome)
);

CREATE TABLE produto_opcao (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    obrigatorio BOOLEAN NOT NULL DEFAULT FALSE,
    multipla_escolha BOOLEAN NOT NULL DEFAULT FALSE,
    ordem INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_produto_opcao_produto
        FOREIGN KEY (produto_id)
        REFERENCES produto (id),

    CONSTRAINT uk_produto_opcao_nome
        UNIQUE (produto_id, nome)
);

CREATE TABLE produto_opcao_valor (
    id BIGSERIAL PRIMARY KEY,
    produto_opcao_id BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    valor_adicional NUMERIC(10, 2) NOT NULL DEFAULT 0,
    quantidade_minima INTEGER,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ordem INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT fk_produto_opcao_valor_opcao
        FOREIGN KEY (produto_opcao_id)
        REFERENCES produto_opcao (id),

    CONSTRAINT ck_produto_opcao_valor_adicional
        CHECK (valor_adicional >= 0),

    CONSTRAINT ck_produto_opcao_valor_quantidade_minima
        CHECK (quantidade_minima IS NULL OR quantidade_minima > 0),

    CONSTRAINT uk_produto_opcao_valor_nome
        UNIQUE (produto_opcao_id, nome)
);

CREATE TABLE produto_variacao_adicional (
    id BIGSERIAL PRIMARY KEY,
    produto_variacao_id BIGINT NOT NULL,
    produto_opcao_valor_id BIGINT NOT NULL,
    valor_adicional NUMERIC(10, 2) NOT NULL,

    CONSTRAINT fk_produto_variacao_adicional_variacao
        FOREIGN KEY (produto_variacao_id)
        REFERENCES produto_variacao (id),

    CONSTRAINT fk_produto_variacao_adicional_valor
        FOREIGN KEY (produto_opcao_valor_id)
        REFERENCES produto_opcao_valor (id),

    CONSTRAINT ck_produto_variacao_adicional_valor
        CHECK (valor_adicional >= 0),

    CONSTRAINT uk_produto_variacao_adicional
        UNIQUE (produto_variacao_id, produto_opcao_valor_id)
);

CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    usuario_id BIGINT,
    data_entrega DATE NOT NULL,
    horario_entrega TIME NOT NULL,
    tipo_entrega VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    observacao TEXT,
    valor_produtos NUMERIC(10, 2) NOT NULL DEFAULT 0,
    valor_entrega NUMERIC(10, 2) NOT NULL DEFAULT 0,
    valor_desconto NUMERIC(10, 2) NOT NULL DEFAULT 0,
    valor_total NUMERIC(10, 2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente (id),

    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario (id),

    CONSTRAINT ck_pedido_valor_produtos
        CHECK (valor_produtos >= 0),

    CONSTRAINT ck_pedido_valor_entrega
        CHECK (valor_entrega >= 0),

    CONSTRAINT ck_pedido_valor_desconto
        CHECK (valor_desconto >= 0),

    CONSTRAINT ck_pedido_valor_total
        CHECK (valor_total >= 0)
);

CREATE TABLE pedido_item (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    produto_variacao_id BIGINT,
    quantidade INTEGER NOT NULL,
    valor_unitario NUMERIC(10, 2) NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,
    observacao TEXT,

    CONSTRAINT fk_pedido_item_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido (id),

    CONSTRAINT fk_pedido_item_produto
        FOREIGN KEY (produto_id)
        REFERENCES produto (id),

    CONSTRAINT fk_pedido_item_variacao
        FOREIGN KEY (produto_variacao_id)
        REFERENCES produto_variacao (id),

    CONSTRAINT ck_pedido_item_quantidade
        CHECK (quantidade > 0),

    CONSTRAINT ck_pedido_item_valor_unitario
        CHECK (valor_unitario >= 0),

    CONSTRAINT ck_pedido_item_valor_total
        CHECK (valor_total >= 0)
);

CREATE TABLE item_pedido_opcao (
    id BIGSERIAL PRIMARY KEY,
    pedido_item_id BIGINT NOT NULL,
    produto_opcao_id BIGINT NOT NULL,
    produto_opcao_valor_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 1,
    valor_adicional NUMERIC(10, 2) NOT NULL DEFAULT 0,

    CONSTRAINT fk_item_pedido_opcao_item
        FOREIGN KEY (pedido_item_id)
        REFERENCES pedido_item (id),

    CONSTRAINT fk_item_pedido_opcao_opcao
        FOREIGN KEY (produto_opcao_id)
        REFERENCES produto_opcao (id),

    CONSTRAINT fk_item_pedido_opcao_valor
        FOREIGN KEY (produto_opcao_valor_id)
        REFERENCES produto_opcao_valor (id),

    CONSTRAINT ck_item_pedido_opcao_quantidade
        CHECK (quantidade > 0),

    CONSTRAINT ck_item_pedido_opcao_valor
        CHECK (valor_adicional >= 0)
);

CREATE TABLE pedido_item_imagem (
    id BIGSERIAL PRIMARY KEY,
    pedido_item_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    ordem INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pedido_item_imagem_item
        FOREIGN KEY (pedido_item_id)
        REFERENCES pedido_item (id),

    CONSTRAINT ck_pedido_item_imagem_ordem
        CHECK (ordem >= 0)
);

CREATE TABLE endereco (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(9) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_endereco_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente (id)
);

CREATE TABLE pedido_endereco (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL UNIQUE,
    logradouro VARCHAR(200) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(9) NOT NULL,

    CONSTRAINT fk_pedido_endereco_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido (id)
);

CREATE TABLE pagamento (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    valor NUMERIC(10, 2) NOT NULL,
    percentual NUMERIC(5, 2),
    parcelas INTEGER,
    data_pagamento TIMESTAMP,
    link_pagamento TEXT,
    observacao TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pagamento_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido (id),

    CONSTRAINT ck_pagamento_valor
        CHECK (valor >= 0),

    CONSTRAINT ck_pagamento_percentual
        CHECK (
            percentual IS NULL
            OR (percentual >= 0 AND percentual <= 100)
        ),

    CONSTRAINT ck_pagamento_parcelas
        CHECK (parcelas IS NULL OR parcelas > 0)
);