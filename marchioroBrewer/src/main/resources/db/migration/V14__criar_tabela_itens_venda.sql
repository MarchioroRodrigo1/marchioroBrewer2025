CREATE TABLE item_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10, 2) NOT NULL,
    
    -- Chave Estrangeira para Venda
    codigo_venda BIGINT NOT NULL,
    
    -- Chave Estrangeira para Cerveja
    codigo_cerveja BIGINT NOT NULL,

    -- Definição das Constraints de integridade
    CONSTRAINT fk_item_venda_venda FOREIGN KEY (codigo_venda) REFERENCES venda (id),
    CONSTRAINT fk_item_venda_cerveja FOREIGN KEY (codigo_cerveja) REFERENCES cerveja (id)
);
