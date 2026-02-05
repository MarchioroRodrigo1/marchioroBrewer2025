-- Tabela principal: grupo
CREATE TABLE grupo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_grupo VARCHAR(50) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabela de ligação: grupo_permissao (Relacionamento ManyToMany)
CREATE TABLE grupo_permissao (
    codigo_grupo BIGINT NOT NULL,
    codigo_permissao BIGINT NOT NULL,
    
    PRIMARY KEY (codigo_grupo, codigo_permissao),
    CONSTRAINT fk_grupo_permissao_grupo FOREIGN KEY (codigo_grupo) REFERENCES grupo (id),
    CONSTRAINT fk_grupo_permissao_permissao FOREIGN KEY (codigo_permissao) REFERENCES permissao (id)
);