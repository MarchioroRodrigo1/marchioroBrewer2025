CREATE TABLE permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome_permissao VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uk_permissao_nome UNIQUE (nome_permissao)
) ENGINE=InnoDB;

INSERT INTO permissao (id, nome_permissao, ativo) VALUES
(1, 'CADASTRAR_USUARIO', true),
(2, 'LISTAR_USUARIO', true),
(3, 'EDITAR_USUARIO', true),
(4, 'EXCLUIR_USUARIO', true),
(5, 'CADASTRAR_CLIENTE', true),
(6, 'LISTAR_CLIENTE', true),
(7, 'EDITAR_CLIENTE', true),
(8, 'EXCLUIR_CLIENTE', true),
(9, 'CADASTRAR_CERVEJA', true),
(10, 'LISTAR_CERVEJA', true),
(11, 'EDITAR_CERVEJA', true),
(12, 'EXCLUIR_CERVEJA', true),
(13, 'REALIZAR_VENDA', true),
(14, 'LISTAR_VENDA', true),
(15, 'GERENCIAR_GRUPO', true),
(16, 'GERENCIAR_PERMISSAO', true);
