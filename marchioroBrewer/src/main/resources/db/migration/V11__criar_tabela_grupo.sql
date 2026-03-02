-- Certifique-se de que a tabela 'permissao' já foi criada antes desta!

-- Tabela principal: grupo
CREATE TABLE grupo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_grupo VARCHAR(50) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- Tabela de ligação: grupo_permissao
CREATE TABLE grupo_permissao (
    codigo_grupo BIGINT NOT NULL,
    codigo_permissao BIGINT NOT NULL,
    
    PRIMARY KEY (codigo_grupo, codigo_permissao),
    CONSTRAINT fk_grupo_permissao_grupo FOREIGN KEY (codigo_grupo) REFERENCES grupo (id) ON DELETE CASCADE,
    CONSTRAINT fk_grupo_permissao_permissao FOREIGN KEY (codigo_permissao) REFERENCES permissao (id) ON DELETE CASCADE
) ENGINE=InnoDB; -- Ajustado aqui

-- Inserts (O MySQL aceita 1 ou true para o campo BOOLEAN)
INSERT INTO grupo (id, nome_grupo, ativo) VALUES 
(1, 'Administrador', 1),
(2, 'Vendedor', 1),
(3, 'Gerente', 1),
(4, 'Estoque', 1);
