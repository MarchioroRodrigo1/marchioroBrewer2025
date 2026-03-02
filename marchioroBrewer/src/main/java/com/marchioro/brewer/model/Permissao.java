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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "permissao")
public class Permissao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome da permissão é obrigatório")
    @Column(nullable = false, length = 50, unique = true)
    private String nomePermissao; 
    
   // @NotNull(message = "A permição é obrigatório") excluir
    @ManyToMany(mappedBy = "permissoes", fetch = FetchType.LAZY) // Indica que 'permissoes' é o campo na classe Grupo
    private Set<Grupo> grupos = new HashSet<>(); 
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    public Permissao() {

	}
	// Getters e Setters
    public Long getId() { 
    	return id; 
    	}
    public void setId(Long id) { 
    	this.id = id; 
    	}

    public String getNomePermissao() {
    	return nomePermissao; 
    	}
    public void setNomePermissao(String nomePermissao) { 
    	this.nomePermissao = nomePermissao; 
    	}

    

    public Set<Grupo> getGrupos() {
		return grupos;
	}
	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}
	 public Boolean getAtivo() {
	        return ativo;
	    }

	    public void setAtivo(Boolean ativo) {
	        this.ativo = ativo;
	    }
    
    // hashCode e equals baseados no ID
    
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
		Permissao other = (Permissao) obj;
		return Objects.equals(id, other.id);
	}  
}


