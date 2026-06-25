package com.marchioro.brewer.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marchioro.brewer.dto.ItemVendaDTO;
import com.marchioro.brewer.model.Cerveja;
import com.marchioro.brewer.model.ItemVenda;
import com.marchioro.brewer.model.StatusVenda;
import com.marchioro.brewer.model.Venda;
import com.marchioro.brewer.repository.CervejasRepository;
import com.marchioro.brewer.repository.ClienteRepository;
import com.marchioro.brewer.repository.UsuarioRepository;
import com.marchioro.brewer.service.VendaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private CervejasRepository cervejasRepository;

    private final VendaService vendaService;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public VendasController(VendaService vendaService,
                            ClienteRepository clienteRepository,
                            UsuarioRepository usuarioRepository) {
        this.vendaService = vendaService;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // =========================================================
    // FORM NOVO
    // =========================================================
    @GetMapping("/novo")
    public String novo(Venda venda, Model model) {

        model.addAttribute("clientes", clienteRepository.findByAtivoTrue());
        model.addAttribute("vendedores", usuarioRepository.findByAtivoTrue());
        model.addAttribute("statusList", StatusVenda.values());

        return "venda/CadastroVenda";
    }

    // =========================================================
    // SALVAR
    // =========================================================
    @PostMapping("/novo")
    public String salvar(@Valid Venda venda,
                         BindingResult result,
                         @RequestParam(value = "itensJson", required = false) String itensJson,
                         Model model,
                         RedirectAttributes attributes) {

        // 1. Processar o JSON de itens enviado pelo JavaScript do front-end
        if (itensJson != null && !itensJson.trim().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                
                // Converte a string JSON usando o SEU ItemVendaDTO já existente
                java.util.List<ItemVendaDTO> dtos = mapper.readValue(itensJson, 
                    new com.fasterxml.jackson.core.type.TypeReference<java.util.List<ItemVendaDTO>>() {});

                // Limpa a lista atual da venda para evitar duplicidade
                venda.getItens().clear();

                // Percorre os itens vindos da tela e monta as entidades JPA
                for (ItemVendaDTO dto : dtos) {
                    ItemVenda item = new ItemVenda();
                    
                   
                    Cerveja cerveja = cervejasRepository.findById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado. ID: " + dto.getId()));
                    
                    
                    item.setCerveja(cerveja); 
                    item.setQuantidade(dto.getQuantidade());
                    // Convertendo o Double do seu DTO para BigDecimal que sua entidade Venda espera
                    item.setValorUnitario(BigDecimal.valueOf(dto.getValor()));
                    
                    // IMPORTANTE: Define o relacionamento bidirecional para o Hibernate salvar o venda_id
                    item.setVenda(venda); 

                    venda.getItens().add(item);
                }

                // Recalcula o valor total da venda no Java por segurança
                venda.calcularTotal();

            } catch (Exception e) {
                // Caso ocorra algum erro na conversão do JSON
                result.rejectValue("itens", "error.itens", "Erro ao processar os itens da venda: " + e.getMessage());
            }
        }

        // 2. Se houver erros de validação (Campos obrigatórios vazios, etc.)
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteRepository.findByAtivoTrue());
            model.addAttribute("vendedores", usuarioRepository.findByAtivoTrue());
            model.addAttribute("statusList", StatusVenda.values());
            return "venda/CadastroVenda";
        }

        // 3. Salva a venda e seus itens de forma cascata
        vendaService.salvar(venda);
        
       // attributes.addFlashAttribute("mensagem", "Venda salva com sucesso!");
   
        // Define data
        venda.setDataCriacao(LocalDate.now());

        vendaService.salvar(venda);

        attributes.addFlashAttribute("mensagemSucesso", "Venda salva com sucesso!");
        
        System.out.println("JSON RECEBIDO: " + itensJson);

        return "redirect:/vendas";
    }
    
    

    // =========================================================
    // LISTAR
    // =========================================================
    @GetMapping
    public String listar(Pageable pageable, Model model) {

        Page<Venda> page = vendaService.listar(pageable);

        model.addAttribute("page", page);
        model.addAttribute("vendas", page.getContent());

        return "venda/ListarVendas";
    }

    // =========================================================
    // EDITAR
    // =========================================================
    @GetMapping("/{id}")
    public String editar(@PathVariable Long id, Model model) throws Exception {

    	Venda venda = vendaService.buscarVendaCompleta(id);

        ObjectMapper mapper = new ObjectMapper();

        String itensJson = mapper.writeValueAsString(
                venda.getItens().stream()
                        .map(item -> Map.of(
                                "id", item.getCerveja().getId(),
                                "nome", item.getCerveja().getNome(),
                                "valor", item.getValorUnitario(),
                                "quantidade", item.getQuantidade(),
                                "estoque", item.getCerveja().getQuantidadeEstoque(),
                                "urlImagem", item.getCerveja().getUrlImagem()
                                
                        ))
                        .toList()
        );

        model.addAttribute("clientes", clienteRepository.findByAtivoTrue());
        model.addAttribute("vendedores", usuarioRepository.findByAtivoTrue());
        model.addAttribute("statusList", StatusVenda.values());

        model.addAttribute("venda", venda);
        model.addAttribute("itensCarrinho", itensJson);

        return "venda/CadastroVenda";
    }

    // =========================================================
    // EXCLUIR (SOFT DELETE)
    // =========================================================
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        vendaService.excluir(id);

        return ResponseEntity.ok().build();
    }
    
  
}
