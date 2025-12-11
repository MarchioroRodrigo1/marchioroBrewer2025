-- V2__cria_tabela_cerveja.sql (corrigido)
CREATE TABLE cerveja (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sku VARCHAR(20) NOT NULL,
    nome VARCHAR(80) NOT NULL,               -- corrigido: era "name"
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    teor_alcoolico DECIMAL(5,2),             -- corrigido: "teor_alcoolico" (dois o)
    comissao DECIMAL(5,2),
    origem VARCHAR(20) NOT NULL,
    sabor VARCHAR(20) NOT NULL,
    estilo_id BIGINT NOT NULL,
    quantidade_estoque INT DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_cerveja_estilo FOREIGN KEY (estilo_id) REFERENCES estilo(id)
);
