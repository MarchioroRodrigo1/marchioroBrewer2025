CREATE TABLE permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome_permissao VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_permissao_nome UNIQUE (nome_permissao)
) ENGINE=InnoDB;