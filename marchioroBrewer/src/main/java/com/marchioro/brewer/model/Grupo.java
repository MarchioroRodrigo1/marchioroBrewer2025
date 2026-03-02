package com.marchioro.brewer.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    // permissões carregadas junto (necessário p/ segurança)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "grupo_permissao",
        joinColumns = @JoinColumn(name = "codigo_grupo"),
        inverseJoinColumns = @JoinColumn(name = "codigo_permissao")
    )
    private Set<Permissao> permissoes = new HashSet<>();

    @ManyToMany(mappedBy = "grupos")
    private Set<Usuario> usuarios = new HashSet<>();

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

	public Boolean getAtivo() {
	    return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public void adicionarPermissao(Permissao permissao) {
	    this.permissoes.add(permissao);
	}

	public void removerPermissao(Permissao permissao) {
	    this.permissoes.remove(permissao);
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
