CREATE TABLE regiao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome_regiao VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE INDEX uk_regiao_nome (nome_regiao)
) ENGINE=InnoDB; -- Garante o motor de armazenamento no MySQL

INSERT INTO regiao (nome_regiao, ativo)
VALUES 
    ('Norte', 1),
    ('Nordeste', 1),
    ('Centro-Oeste', 1),
    ('Sudeste', 1),
    ('Sul', 1);
