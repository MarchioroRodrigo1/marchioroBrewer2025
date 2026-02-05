package com.marchioro.brewer.model;

public enum StatusVenda {
	
	ORCAMENTO("Orçamento"),    // Índice 0
    EMITIDA("Emitida"),      // Índice 1
    CANCELADA("Cancelada");  // Índice 2

    private final String descricao;

    StatusVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
