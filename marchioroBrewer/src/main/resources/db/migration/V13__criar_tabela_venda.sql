CREATE TABLE venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_criacao DATE NOT NULL,
    valor_frete DECIMAL(15, 2) NOT NULL,
    valor_desconto DECIMAL(15, 2) NOT NULL,
    valor_total DECIMAL(15, 2) NOT NULL,
    observacao VARCHAR(255) NOT NULL,
    data_entrega DATE NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- O EnumType.ORDINAL salva o índice do Enum como INT
    status TINYINT NOT NULL, 

    -- Chaves Estrangeiras (ManyToOne)
    codigo_vendedor BIGINT NOT NULL,
    codigo_cliente BIGINT NOT NULL,

    CONSTRAINT fk_venda_vendedor FOREIGN KEY (codigo_vendedor) REFERENCES usuario (id),
    CONSTRAINT fk_venda_cliente FOREIGN KEY (codigo_cliente) REFERENCES cliente (id)
);
