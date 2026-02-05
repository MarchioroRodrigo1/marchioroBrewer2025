package com.marchioro.brewer.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "grupo")
public class Grupo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome do grupo é obrigatório")
	@Column(nullable = false, length = 50, unique = true)
	private String nomeGrupo;

	@Column(nullable = false)
	private Boolean ativo = true;

	@ManyToMany
	@JoinTable(name = "grupo_permissao", // Nome da tabela de ligação no banco
			joinColumns = @JoinColumn(name = "codigo_grupo"), // FK para Grupo
			inverseJoinColumns = @JoinColumn(name = "codigo_permissao") // FK para Permissao
	)
	private List<Permissao> permissoes; // Alterado de List<Grupo> para List<Permissao>

	@ManyToMany(mappedBy = "grupos") // Indica que o mapeamento principal está no atributo 'grupos' da classe Usuario
	private List<Usuario> usuarios;

	public Grupo() {
	}

	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeGrupo() {
		return nomeGrupo;
	}

	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}

	public Boolean isAtivo() {
		return Boolean.TRUE.equals(ativo);
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	// Equals e HashCode baseados no ID

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
		Grupo other = (Grupo) obj;
		return Objects.equals(id, other.id);
	}

}
