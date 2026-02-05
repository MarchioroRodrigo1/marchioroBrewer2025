CREATE TABLE estado (
    id BIGINT NOT NULL AUTO_INCREMENT,
    codigo_ibge_uf VARCHAR(2) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    sigla VARCHAR(2) NOT NULL,
    regiao_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_estado_codigo_ibge UNIQUE (codigo_ibge_uf),
    CONSTRAINT fk_estado_regiao FOREIGN KEY (regiao_id) REFERENCES regiao (id)
) ENGINE=InnoDB;
