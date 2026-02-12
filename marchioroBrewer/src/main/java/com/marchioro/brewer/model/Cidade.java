package com.marchioro.brewer.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cidade")
public class Cidade  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @NotBlank(message = "O código IBGE da cidade da cidade deve ser preenchido")
    @Column(name = "codigo_ibge", nullable = false, length = 8)
	private String codigoIBGE;
	
    @NotBlank(message = "O nome da cidade deve ser preenchido")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres") 
    @Column(nullable = false, length = 50)
	private String nomeCidade;
    
    @NotBlank(message = "O nome da cidade deve ser preenchido")
    @Column(nullable = false, length = 2)
    private String uf; 
    
    @Column(nullable = false)
	private Boolean ativo = true;
    
    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

	public Cidade() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public Boolean isAtivo() {
		return Boolean.TRUE.equals(ativo);
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidade other = (Cidade) obj;
		return Objects.equals(id, other.id);
	}

}
