-- Tabela principal: usuario
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(30) NOT NULL,
    email_usuario VARCHAR(50) NOT NULL UNIQUE,
    senha_usuario VARCHAR(255) NOT NULL, -- Tamanho recomendado para hashes como BCrypt
    data_nascimento DATE NOT NULL,
    url_avatar varchar(255),
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

INSERT INTO `usuario` VALUES 
(1,'Rodrigo Marchioro','teste@gmail.com','$2a$10$9DsRzNRBpEKxWpq/HgL3K.zF9il.lPfrrqwQKMimHHXexkZCy0Hky','1977-08-25',null,1),
(2,'Arnaldo Antunes','arnaldo@teste.com.br','$2a$10$WM4dQxUSzliPCfl9f05Ypet9f7h3wxwskTT7myRxawg1Bw8kZ.j2i','2026-03-02',null,1),
(3,'Wanessa Camargo','wanessa@gmail.com','$2a$10$9Ku8pnMu1NDy1sjh9cSx9.WFqpO.JDQvNpuprfqIPFbCz5n.MvMra','2026-03-02',null,1);

INSERT INTO `usuario_grupo` VALUES (1,1),(2,2),(3,2),(2,3),(3,3),(3,4);

