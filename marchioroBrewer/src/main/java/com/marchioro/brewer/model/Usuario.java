package com.marchioro.brewer.model;

import java.io.Serializable;
import java.time.LocalDate;
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable{

	 private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank(message = "O nome de usuário é obrigatório")
	    @Size(min = 4, max = 30, message = "O nome de usuário deve ter entre 4 e 30 caracteres")
	    @Column(nullable = false, length = 30)
	    private String nomeUsuario;

	    @NotBlank(message = "O email é obrigatório")
	    @Email(message = "Não parece ser um e-mail válido")
	    @Column(nullable = false, length = 50, unique = true)
	    private String emailUsuario;

	    @NotBlank(message = "A senha é obrigatório")
	    @Column(nullable = false) // Em 2025, não limite o length aqui se for usar BCrypt (senhas criptografadas são longas)
	    private String senhaUsuario;

	    @NotNull(message = "A data de nascimento é obrigatória") // Para datas use @NotNull, não @NotBlank
	    @Column(name = "data_nascimento", nullable = false)
	    private LocalDate dataNascimento; // Corrigido: nascimento (com c)

	    @Column(nullable = false)
	    private Boolean ativo = true;

	    // MAPEAMENTO MUITOS PARA MUITOS
	    @ManyToMany
	    @JoinTable(
	        name = "usuario_grupo", // Nome da tabela de ligação
	        joinColumns = @JoinColumn(name = "codigo_usuario"),
	        inverseJoinColumns = @JoinColumn(name = "codigo_grupo")
	    )
	    private List<Grupo> grupos;

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

		public List<Grupo> getGrupos() {
			return grupos;
		}

		public void setGrupos(List<Grupo> grupos) {
			this.grupos = grupos;
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
