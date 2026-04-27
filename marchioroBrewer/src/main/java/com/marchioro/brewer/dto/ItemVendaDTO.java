package com.marchioro.brewer.dto;

public class ItemVendaDTO {

    private Long id;
    private String nome;
    private Double valor;
    private Integer quantidade;
    private Integer estoque;
	public Integer getEstoque() {
		return estoque;
	}
	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}
	public Long getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public Double getValor() {
		return valor;
	}
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
    
    
}
