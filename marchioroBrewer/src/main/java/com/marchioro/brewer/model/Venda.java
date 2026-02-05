package com.marchioro.brewer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "venda")
public class Venda implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	@Column(name = "data_criacao", nullable = false)
	private LocalDate dataCriacao;
	
    @NotNull(message = "O valor do frete é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2)
    @Column(name = "valor_frete", precision = 15, scale = 2)
	private BigDecimal valorFrete;
	
    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.00", message = "O valor deve ser maior ou igual a zero")
    @Digits(integer = 13, fraction = 2)
    @Column(name = "valor_desconto", precision = 15, scale = 2)
	private BigDecimal valorDesconto;
	
    @NotNull(message = "O valor total é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2)
    @Column(name = "valor_total", precision = 15, scale = 2)
	private BigDecimal valorTotal;
	
    @NotBlank(message = "A observação é obrigatória")
    @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres")
    @Column(nullable = false, length = 255)
	private String observacao;
	
    @NotNull(message = "A data da entrega é obrigatório")
    @Column(name = "data_entrega", nullable = false)
	private LocalDate dataEntrega;
	
    // Relacionamento ManyToOne (Muitas vendas para Um vendedor)
    @NotNull(message = "O vendedor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_vendedor", nullable = false)
    private Usuario vendedor;
	
	 // Relacionamento ManyToOne (Muitas vendas para Um cliente)
    @NotNull(message = "O cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "codigo_cliente", nullable = false)
    private Cliente cliente;
	
    // Relacionamento OneToMany (Uma venda para Muitos itens de venda)
    // Usamos CascadeType.ALL para garantir que ao salvar a Venda, os itens também sejam salvos/atualizados.
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens;
	
 // Relacionamento com StatusVenda, usando INT/ORDINAL
    @NotNull(message = "O status da venda é obrigatório")
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private StatusVenda status;
	 
	 @Column(nullable = false)
	 private Boolean ativo = true;

	 public Venda() {
		
	 }

	 public Long getId() {
		 return id;
	 }

	 public void setId(Long id) {
		 this.id = id;
	 }

	 public LocalDate getDataCriacao() {
		 return dataCriacao;
	 }

	 public void setDataCriacao(LocalDate dataCriacao) {
		 this.dataCriacao = dataCriacao;
	 }

	 public BigDecimal getValorFrete() {
		 return valorFrete;
	 }

	 public void setValorFrete(BigDecimal valorFrete) {
		 this.valorFrete = valorFrete;
	 }

	 public BigDecimal getValorDesconto() {
		 return valorDesconto;
	 }

	 public void setValorDesconto(BigDecimal valorDesconto) {
		 this.valorDesconto = valorDesconto;
	 }

	 public BigDecimal getValorTotal() {
		 return valorTotal;
	 }

	 public void setValorTotal(BigDecimal valorTotal) {
		 this.valorTotal = valorTotal;
	 }

	 public String getObservacao() {
		 return observacao;
	 }

	 public void setObservacao(String observacao) {
		 this.observacao = observacao;
	 }

	 public LocalDate getDataEntrega() {
		 return dataEntrega;
	 }

	 public void setDataEntrega(LocalDate dataEntrega) {
		 this.dataEntrega = dataEntrega;
	 }

	 public Usuario getVendedor() {
		 return vendedor;
	 }

	 public void setVendedor(Usuario vendedor) {
		 this.vendedor = vendedor;
	 }

	 public Cliente getCliente() {
		 return cliente;
	 }

	 public void setCliente(Cliente cliente) {
		 this.cliente = cliente;
	 }

	 public List<ItemVenda> getItens() {
		 return itens;
	 }

	 public void setItens(List<ItemVenda> itens) {
		 this.itens = itens;
	 }	 

	   public StatusVenda getStatus() {
		return status;
	}

	 public void setStatus(StatusVenda status) {
		 this.status = status;
	 }

	   public Boolean isAtivo() {
			return ativo;
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
		Venda other = (Venda) obj;
		return Objects.equals(id, other.id);
	 }
	 
	 

}
