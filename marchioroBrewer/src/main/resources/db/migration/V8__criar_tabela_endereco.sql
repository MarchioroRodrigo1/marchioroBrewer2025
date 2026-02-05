CREATE TABLE endereco (
    id BIGINT NOT NULL AUTO_INCREMENT,
    logradouro VARCHAR(50) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(50),
    cep VARCHAR(8) NOT NULL,
    codigo_cidade BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_endereco_cidade FOREIGN KEY (codigo_cidade) REFERENCES cidade (id)
) ENGINE=InnoDB;
