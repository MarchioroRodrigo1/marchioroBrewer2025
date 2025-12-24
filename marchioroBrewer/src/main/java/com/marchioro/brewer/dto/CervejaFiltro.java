package com.marchioro.brewer.dto;

/**
 * DTO responsável por representar os filtros
 * da tela de listagem de cervejas.
 *
 * Ele NÃO é uma entidade
 * Ele NÃO é persistido no banco
 *
 * Serve apenas para transporte de dados
 * entre Controller, Service e View.
 */


public class CervejaFiltro {
	
	
	 // ------------------------------------------------------------
    // ID do estilo selecionado no nome
    // ------------------------------------------------------------
	private String nome;
	
	
	 // ------------------------------------------------------------
    // ID do estilo selecionado no nome
    // ------------------------------------------------------------
	private Long estilo;

    // ------------------------------------------------------------
    // Construtor padrão (obrigatório para Spring)
    // ------------------------------------------------------------
	public CervejaFiltro() {
		
	}

    // ------------------------------------------------------------
    // Getters e Setters
    // ------------------------------------------------------------
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getEstilo() {
		return estilo;
	}

	public void setEstilo(Long estilo) {
		this.estilo = estilo;
	}
	
	
	

}
