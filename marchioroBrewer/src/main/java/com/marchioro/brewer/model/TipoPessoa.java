package com.marchioro.brewer.model;
public enum TipoPessoa {
    FISICA("Física"),    // Índice 0
    JURIDICA("Jurídica"); // Índice 1

    private final String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}