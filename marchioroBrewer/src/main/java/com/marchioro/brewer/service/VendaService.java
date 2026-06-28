package com.marchioro.brewer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marchioro.brewer.dto.ItemVendaDTO;
import com.marchioro.brewer.model.Cerveja;
import com.marchioro.brewer.model.ItemVenda;
import com.marchioro.brewer.model.Venda;
import com.marchioro.brewer.repository.CervejasRepository;
import com.marchioro.brewer.repository.VendaRepository;

@Service
public class VendaService {
	private final VendaRepository vendaRepository;
	private final CervejasRepository cervejasRepository;

	public VendaService(VendaRepository vendaRepository, CervejasRepository cervejasRepository) {

		this.vendaRepository = vendaRepository;
		this.cervejasRepository = cervejasRepository;
	}
	
	@Transactional
	public void salvar(Venda vendaFormulario, String itensJson) {

	    Venda venda;

	    // =====================================================
	    // NOVA VENDA OU EDIÇÃO?
	    // =====================================================
	    if (vendaFormulario.getId() == null) {

	        // Nova venda
	        venda = vendaFormulario;

	    } else {

	        // Venda existente
	        venda = buscarVendaCompleta(vendaFormulario.getId());

	        if (venda == null) {
	            throw new RuntimeException("Venda não encontrada.");
	        }

	        // Atualiza apenas os campos simples
	        venda.setCliente(vendaFormulario.getCliente());
	        venda.setVendedor(vendaFormulario.getVendedor());
	        venda.setObservacao(vendaFormulario.getObservacao());
	        venda.setDataEntrega(vendaFormulario.getDataEntrega());
	        venda.setStatus(vendaFormulario.getStatus());
	        venda.setValorFrete(vendaFormulario.getValorFrete());
	        venda.setValorDesconto(vendaFormulario.getValorDesconto());
	    }

	    // Ainda vamos melhorar este método na próxima etapa
	    prepararItensVenda(venda, itensJson);

	    salvarVenda(venda);
	}
	

	@Transactional
	public void salvarVenda(Venda venda) {

	    // Recalcula sempre o valor total da venda
	    venda.calcularTotal();

	    // Garante o relacionamento bidirecional
	    if (venda.getItens() != null) {
	        venda.getItens().forEach(item -> item.setVenda(venda));
	    }

	    vendaRepository.save(venda);
	}

	@Transactional
	public void prepararItensVenda(Venda venda, String itensJson) {

	    if (itensJson == null || itensJson.isBlank()) {
	        return;
	    }

	    try {

	        ObjectMapper mapper = new ObjectMapper();

	        List<ItemVendaDTO> dtos =
	                mapper.readValue(
	                        itensJson,
	                        new TypeReference<List<ItemVendaDTO>>() {});

	        /*
	         * Mapa contendo os itens existentes da venda.
	         * chave = ID do ItemVenda
	         */
	        Map<Long, ItemVenda> itensExistentes =
	                venda.getItens()
	                     .stream()
	                     .filter(i -> i.getId() != null)
	                     .collect(Collectors.toMap(
	                             ItemVenda::getId,
	                             Function.identity()
	                     ));

	        /*
	         * Nova lista de itens da venda.
	         */
	        List<ItemVenda> novaLista = new ArrayList<>();

	        for (ItemVendaDTO dto : dtos) {

	            ItemVenda item;

	            /*
	             * Se veio itemId significa que o item já existe.
	             */
	            if (dto.getItemId() != null) {

	                item = itensExistentes.get(dto.getItemId());

	                if (item == null) {
	                    item = new ItemVenda();
	                    item.setId(dto.getItemId());
	                }

	            } else {

	                /*
	                 * Item novo.
	                 */
	                item = new ItemVenda();
	            }

	            Cerveja cerveja = cervejasRepository
	                    .findById(dto.getId())
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Produto não encontrado: " + dto.getId()));

	            item.setVenda(venda);
	            item.setCerveja(cerveja);
	            item.setQuantidade(dto.getQuantidade());
	            item.setValorUnitario(BigDecimal.valueOf(dto.getValor()));

	            novaLista.add(item);
	        }

	        /*
	         * orphanRemoval=true removerá automaticamente
	         * apenas os itens que ficaram fora da lista.
	         */
	        venda.getItens().clear();
	        venda.getItens().addAll(novaLista);

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Erro processando itens da venda.",
	                e);
	    }
	}

	@Transactional
    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));
    }

    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarPorId(id);
        venda.setAtivo(false);
        vendaRepository.save(venda);
    }
    
    public Venda buscarVendaCompleta(Long id){

        return vendaRepository.buscarVendaCompleta(id);
    }
    
    /*
    @Transactional
    public Venda atualizar(Long id) {

        Venda vendaBanco = vendaRepository.buscarVendaCompleta(id);

        if (vendaBanco == null) {
            throw new RuntimeException("Venda não encontrada.");
        }

        return vendaBanco;
    }
   */ 
    public Page<Venda> listar(Pageable pageable) {
        return vendaRepository.findByAtivoTrue(pageable);
    }

}