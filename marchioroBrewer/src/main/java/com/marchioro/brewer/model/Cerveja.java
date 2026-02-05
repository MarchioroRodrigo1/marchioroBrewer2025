package com.marchioro.brewer.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cerveja")
public class Cerveja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* SKU – obrigatório e curto */
    @NotBlank(message = "SKU é obrigatório")
    @Size(max = 50, message = "SKU deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String sku;

    /* Nome – obrigatório */
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    /* Descrição – obrigatória */
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    @Column(nullable = false, length = 255)
    private String descricao;

    /* Valor da cerveja */
    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2)
    @Column(precision = 15, scale = 2)
    private BigDecimal valor;

    /* Teor alcoólico */
    @NotNull(message = "Informe o teor alcoólico")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "O teor alcoólico deve ser maior que 0")
    @DecimalMax(value = "100.0",
            message = "O teor alcoólico não pode ser maior que 100")
    @Column(nullable = false, name = "teor_alcoolico", precision = 5, scale = 2)
    private BigDecimal teorAlcoolico;

    /* Comissão */
    @NotNull(message = "Informe a comissao")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "A comissao deve ser maior ou igual a 0")
    @DecimalMax(value = "100.0",
            message = "A comissao não pode ser maior que 100")
    @Column(nullable = false, name = "comissao", precision = 5, scale = 2)
    private BigDecimal comissao;

    /* Origem (ENUM) */
    @NotNull(message = "A origem deve ser informda")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Origem origem;

    /* Sabor (ENUM) */
    @NotNull(message = "selecione um Sabor")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sabor sabor;

    /* Estilo → FK */
    @NotNull(message = "Selecione um estilo")
    @ManyToOne(optional = false)
    @JoinColumn(name = "estilo_id", nullable = false)
    private Estilo estilo;

    /* Quantidade em estoque */
    @NotNull(message = "Informe a quantidade em estoque")
    @Min(value = 0, message = "O estoque deve ser maior ou igual a 0")
    @Column(name = "quantidade_estoque")
    private Integer quantidadeEstoque;
    
    // Uma cerveja pode estar em muitos itens de venda
    @OneToMany(mappedBy = "cerveja")
    private List<ItemVenda> itensVenda;
    
    /*Ativo*/
    @Column(nullable = false)
    private boolean ativo = true;

    public Cerveja() {}

    // Getters e Setters -----------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getTeorAlcoolico() {
        return teorAlcoolico;
    }

    public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
        this.teorAlcoolico = teorAlcoolico;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public Origem getOrigem() {
        return origem;
    }

    public void setOrigem(Origem origem) {
        this.origem = origem;
    }

    public Sabor getSabor() {
        return sabor;
    }

    public void setSabor(Sabor sabor) {
        this.sabor = sabor;
    }

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
    
    // equals() e hashCode usando id ------------------


	@Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Cerveja))
            return false;
        Cerveja other = (Cerveja) obj;
        return Objects.equals(id, other.id);
    }
}
