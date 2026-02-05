package com.marchioro.brewer.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String nomeCliente;

    @NotBlank(message = "O CPF ou CNPJ é obrigatório")
    @Column(nullable = false, length = 14)
    private String documento;

    @NotBlank(message = "O e-mail é obrigatório")
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(nullable = false, length = 15)
    private String telefone;

    // MAPEAMENTO: Um cliente possui um endereço. 
    // O JPA criará a coluna 'endereco_id' na tabela cliente.
    @OneToOne(cascade = CascadeType.ALL) // CascadeType.ALL faz com que, ao salvar o cliente, o endereço também seja salvo
    @JoinColumn(name = "endereco_id")
    @Valid
    private Endereco endereco;
    
    @NotNull(message = "O tipo de pessoa é obrigatório")
    @Enumerated(EnumType.ORDINAL) // Aqui ele define que salvará 0 ou 1 no banco
    @Column(name = "tipo_pessoa", nullable = false)
    private TipoPessoa tipoPessoa;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    public Cliente() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
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
		Cliente other = (Cliente) obj;
		return Objects.equals(id, other.id);
	}
    
    
}
