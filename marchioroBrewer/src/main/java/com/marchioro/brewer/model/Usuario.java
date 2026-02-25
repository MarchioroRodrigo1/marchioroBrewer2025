package com.marchioro.brewer.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.persistence.Transient;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@GroupSequence({Usuario.class, OnCreate.class, OnUpdate.class})
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 30, message = "O nome de usuário deve ter entre 4 e 30 caracteres")
    @Column(nullable = false, length = 30)
    private String nomeUsuario;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Não parece ser um e-mail válido")
    @Column(nullable = false, length = 50, unique = true)
    private String emailUsuario;

   // @NotBlank(message = "A senha é obrigatório")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
    	    message = "A senha deve conter: 8 caracteres, maiúscula, minúscula, número e caractere especial")
    @Column(nullable = false)
    private String senhaUsuario;
    
    @Transient
    private String confirmacaoSenha;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Column(name = "data_nascimento", nullable = false)
    @PastOrPresent(message = "A data de nascimento não pode ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private Boolean ativo = true;

   
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_grupo",
        joinColumns = @JoinColumn(name = "codigo_usuario"),
        inverseJoinColumns = @JoinColumn(name = "codigo_grupo")
    )
    private Set<Grupo> grupos = new HashSet<>();

		public Usuario() {
			
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNomeUsuario() {
			return nomeUsuario;
		}

		public void setNomeUsuario(String nomeUsuario) {
			this.nomeUsuario = nomeUsuario;
		}

		public String getEmailUsuario() {
			return emailUsuario;
		}

		public void setEmailUsuario(String emailUsuario) {
			this.emailUsuario = emailUsuario;
		}

		public String getSenhaUsuario() {
			return senhaUsuario;
		}

		public void setSenhaUsuario(String senhaUsuario) {
			this.senhaUsuario = senhaUsuario;
		}

		public LocalDate getDataNascimento() {
			return dataNascimento;
		}

		public void setDataNascimento(LocalDate dataNascimento) {
			this.dataNascimento = dataNascimento;
		}

		public Boolean isAtivo() {
			return Boolean.TRUE.equals(ativo);
		}

		public void setAtivo(Boolean ativo) {
			this.ativo = ativo;
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
		
		

		public String getConfirmacaoSenha() {
			return confirmacaoSenha;
		}

		public void setConfirmacaoSenha(String confirmacaoSenha) {
			this.confirmacaoSenha = confirmacaoSenha;
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
			Usuario other = (Usuario) obj;
			return Objects.equals(id, other.id);
		}
	    
	    

}
