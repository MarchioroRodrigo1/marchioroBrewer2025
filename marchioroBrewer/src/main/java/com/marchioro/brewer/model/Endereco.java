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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "endereco")
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Logradouro deve ser preenchido")
    @Column(nullable = false, length = 50)
    private String logradouro;
    
    @NotBlank(message = "O número deve ser preenchido")
    @Column(nullable = false, length = 10)
    private String numero;
    
    @Column(length = 50) // Complemento geralmente não é obrigatório
    private String complemento;
    
    @NotBlank(message = "O CEP deve ser preenchido")
    @Size(min = 8, max = 8, message = "O CEP deve ter 8 caracteres") 
    @Column(nullable = false, length = 8)
    private String cep;
    
    
    // Um endereço tem uma cidade. Muitos endereços podem estar na mesma cidade.
    @NotNull(message = "A cidade é obrigatória")
    @ManyToOne
    @JoinColumn(name = "codigo_cidade")
    private Cidade cidade;

    // O Estado é obtido através da cidade (cidade.getEstado()), 
    // por isso não costuma ser mapeado como coluna no banco de endereço para evitar redundância.
    // Mas se o seu Front-end/Framework exigir o campo transiente para o combo, use @Transient.
    @Transient
    private Estado estado;

    @Column(nullable = false)
    private Boolean ativo = true;

	public Endereco() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Boolean isAtivo() {
		return Boolean.TRUE.equals(ativo);
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
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
		Endereco other = (Endereco) obj;
		return Objects.equals(id, other.id);
	}

	

}
