CREATE TABLE regiao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome_regiao VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE INDEX uk_regiao_nome (nome_regiao)
);
