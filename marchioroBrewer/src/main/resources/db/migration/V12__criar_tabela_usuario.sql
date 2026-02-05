-- Tabela principal: usuario
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(30) NOT NULL,
    email_usuario VARCHAR(50) NOT NULL UNIQUE,
    senha_usuario VARCHAR(255) NOT NULL, -- Tamanho recomendado para hashes como BCrypt
    data_nascimento DATE NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabela de ligação: usuario_grupo (Relacionamento ManyToMany)
-- Criada aqui porque a classe Usuario mapeia o relacionamento
-- Tabela de ligação: usuario_grupo
CREATE TABLE usuario_grupo (
    codigo_usuario BIGINT NOT NULL,
    codigo_grupo BIGINT NOT NULL,
    
    PRIMARY KEY (codigo_usuario, codigo_grupo),
    CONSTRAINT fk_usuario_grupo_usuario FOREIGN KEY (codigo_usuario) REFERENCES usuario (id),
    CONSTRAINT fk_usuario_grupo_grupo FOREIGN KEY (codigo_grupo) REFERENCES grupo (id)
);
