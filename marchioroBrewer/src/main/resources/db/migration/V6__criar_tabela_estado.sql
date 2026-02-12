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

-- 2. Inserção nos Estados
INSERT INTO estado (nome, sigla, codigo_ibge_uf, regiao_id, ativo)
VALUES 
    ('Rondônia', 'RO', 11, 1, TRUE),
    ('Acre', 'AC', 12, 1, TRUE),
    ('Amazonas', 'AM', 13, 1, TRUE),
    ('Roraima', 'RR', 14, 1, TRUE),
    ('Pará', 'PA', 15, 1, TRUE),
    ('Amapá', 'AP', 16, 1, TRUE),
    ('Tocantins', 'TO', 17, 1, TRUE),
    ('Maranhão', 'MA', 21, 2, TRUE),
    ('Piauí', 'PI', 22, 2, TRUE),
    ('Ceará', 'CE', 23, 2, TRUE),
    ('Rio Grande do Norte', 'RN', 24, 2, TRUE),
    ('Paraíba', 'PB', 25, 2, TRUE),
    ('Pernambuco', 'PE', 26, 2, TRUE),
    ('Alagoas', 'AL', 27, 2, TRUE),
    ('Sergipe', 'SE', 28, 2, TRUE),
    ('Bahia', 'BA', 29, 2, TRUE),
    ('Mato Grosso do Sul', 'MS', 50, 3, TRUE),
    ('Mato Grosso', 'MT', 51, 3, TRUE),
    ('Goiás', 'GO', 52, 3, TRUE),
    ('Distrito Federal', 'DF', 53, 3, TRUE),
    ('Minas Gerais', 'MG', 31, 4, TRUE),
    ('Espírito Santo', 'ES', 32, 4, TRUE),
    ('Rio de Janeiro', 'RJ', 33, 4, TRUE),
    ('São Paulo', 'SP', 35, 4, TRUE),
    ('Paraná', 'PR', 41, 5, TRUE),
    ('Santa Catarina', 'SC', 42, 5, TRUE),
    ('Rio Grande do Sul', 'RS', 43, 5, TRUE);
