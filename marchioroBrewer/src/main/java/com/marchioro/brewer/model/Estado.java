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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "estado")
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id // Use a anotação padrão e o nome da variável em minúsculo (id)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O Código IBGE do estado deve ser preenchido")
    @Size(min = 2, max = 2, message = "O código deve ter 2 caracteres")
    @Column(nullable = false, length = 2, unique = true, name = "codigo_ibge_uf" )
    private String codigoUF;
    
    @NotBlank(message = "O nome do estado deve ser preenchido")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres") // Corrigido para 50
    @Column(nullable = false, length = 50)
    private String nome;
    
    @NotBlank(message = "A Sigla do estado deve ser preenchida")
    @Size(min = 2, max = 2, message = "A sigla deve ter 2 caracteres")
    @Column(nullable = false, length = 2)
    private String sigla;
    
    @NotNull(message = "Selecione uma região") // Mensagem corrigida
    @ManyToOne(optional = false)
    @JoinColumn(name = "regiao_id") // Nome da coluna corrigido (era refiao_id)
    private Regiao regiao;
    
    @Column(nullable = false)
    private Boolean ativo = true;

	public Estado() {
		
	}

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Regiao getRegiao() {
		return regiao;
	}



	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	  public Boolean isAtivo() {
			return Boolean.TRUE.equals(ativo);
		}

		public void setAtivo(Boolean ativo) {
			this.ativo = ativo;
		}
		
		

	public String getCodigoUF() {
			return codigoUF;
		}

		public void setCodigoUF(String codigoUF) {
			this.codigoUF = codigoUF;
		}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		return Objects.equals(id, other.id);
	}
}
