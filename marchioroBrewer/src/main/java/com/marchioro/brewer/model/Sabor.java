package com.marchioro.brewer.model;

public enum Sabor {
ADOCICADA("Adocicada"),
AMARGA("Amarga"),
FORTE("Forte"),
FRUTADA("Frutada"),
SUAVE("Suave");
	
	private String descricao;
	
	Sabor(String descricao){
		this.descricao = descricao;
	}
	
	public String setDescricao() {
		return descricao;
	}
}
