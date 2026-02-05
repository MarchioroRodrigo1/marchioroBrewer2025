package com.marchioro.brewer.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "regiao")
public class Regiao implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O nome da região deve ser preenchido")
    @Size(min = 2, max = 20, message = "O nome da região deve ter 2 a 20 caracteres")
    @Column(nullable = false, length = 20, unique = true, name = "nome_regiao" )
    private String nomeRegiao; // Corrigido de mome para nome
    
    @JsonIgnore
    @OneToMany(mappedBy = "regiao") //  deve ser o nome do atributo na classe Estado
    private List<Estado> estados; // Sugestão: plural fica melhor para listas

    @Column(nullable = false)
    private Boolean ativo = true;

    // Construtor vazio
    public Regiao() {}

    // Getters e Setters corrigidos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeRegiao() { return nomeRegiao; }
    public void setNomeRegiao(String nomeRegiao) { this.nomeRegiao = nomeRegiao; }

    public List<Estado> getEstados() { return estados; }
    public void setEstados(List<Estado> estados) { this.estados = estados; }

    public Boolean isAtivo() { return Boolean.TRUE.equals(ativo); }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Regiao other = (Regiao) obj;
        return Objects.equals(id, other.id);
    }
}

