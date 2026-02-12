-- 1. Criar a tabela de cidades corretamente
CREATE TABLE cidade (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo_ibge VARCHAR(8) NOT NULL,
    nome_cidade VARCHAR(50) NOT NULL,
    uf varchar(2) not null,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    estado_id BIGINT,
    PRIMARY KEY (id),
    -- Definindo a FK diretamente na criação para ser mais limpo
    CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id) REFERENCES estado(id)
) ENGINE=InnoDB;