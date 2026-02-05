package com.marchioro.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "item_venda")
public class ItemVenda implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A quantidade é obrigatória")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "O valor unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    // Relacionamento ManyToOne (Muitos itens de venda para Uma única venda)
    @ManyToOne
    @JoinColumn(name = "codigo_venda", nullable = false)
    private Venda venda;

    // Relacionamento ManyToOne (Muitos itens de venda para Uma única cerveja)
    @ManyToOne
    @JoinColumn(name = "codigo_cerveja", nullable = false)
    private Cerveja cerveja;
    
    public ItemVenda() {

	}
    
 // --- Getters e Setters, Hashcode e Equals ---

	public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }

    public Cerveja getCerveja() { return cerveja; }
    public void setCerveja(Cerveja cerveja) { this.cerveja = cerveja; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemVenda other = (ItemVenda) obj;
        return Objects.equals(id, other.id);
    }
}
