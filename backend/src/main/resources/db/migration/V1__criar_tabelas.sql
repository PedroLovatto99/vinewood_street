CREATE TABLE usuarios (
      id BIGSERIAL PRIMARY KEY,
      nome VARCHAR(255) NOT NULL,
      email VARCHAR(255) NOT NULL,
      senha VARCHAR(255) NOT NULL,
      saldo_caixa DOUBLE PRECISION NOT NULL
);

CREATE TABLE empresas (
      id BIGSERIAL PRIMARY KEY,
      sigla VARCHAR(255) NOT NULL UNIQUE,
      nome VARCHAR(255) NOT NULL,
      descricao TEXT,
      preco_atual DOUBLE PRECISION NOT NULL
);


CREATE TABLE acoes_compradas (
     id BIGSERIAL PRIMARY KEY,
     usuario_id BIGINT NOT NULL,
     empresa_id BIGINT NOT NULL,
     quantidade INTEGER NOT NULL,
     preco_medio_compra DOUBLE PRECISION NOT NULL,

     CONSTRAINT fk_acao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
     CONSTRAINT fk_acao_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);

CREATE TABLE historico_preco (
     id BIGSERIAL PRIMARY KEY,
     empresa_id BIGINT,
     preco DOUBLE PRECISION,
     data_hora TIMESTAMP,

     CONSTRAINT fk_historico_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);

CREATE TABLE evento_noticia (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT,
    titulo VARCHAR(255),
    conteudo_gerado TEXT,
    impacto VARCHAR(255),
    percentual_variacao DOUBLE PRECISION,
    data_publicacao TIMESTAMP,

    CONSTRAINT fk_noticia_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);