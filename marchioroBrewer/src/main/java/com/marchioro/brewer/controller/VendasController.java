package com.marchioro.brewer.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
                         Model model,
                         RedirectAttributes attributes,
                         @RequestParam(required = false) String itensJson) {
    	 System.out.println("JSON RECEBIDO: " + itensJson);

        if (result.hasErrors()) {

            model.addAttribute("clientes", clienteRepository.findByAtivoTrue());
            model.addAttribute("vendedores", usuarioRepository.findByAtivoTrue());
            model.addAttribute("statusList", StatusVenda.values());
            
           

            return "venda/CadastroVenda";
        }

        try {
            if (itensJson != null && !itensJson.isEmpty()) {

                ObjectMapper mapper = new ObjectMapper();

                List<ItemVendaDTO> itens = Arrays.asList(
                    mapper.readValue(itensJson, ItemVendaDTO[].class)
                );

                //  AQUI ainda NÃO salva no banco
                // só associa na venda

                for (ItemVendaDTO dto : itens) {

                    Cerveja cerveja = cervejasRepository.findById(dto.getId())
                            .orElseThrow(() -> new RuntimeException("Cerveja não encontrada"));

                    ItemVenda item = new ItemVenda();

                    item.setCerveja(cerveja);
                    item.setQuantidade(dto.getQuantidade());

                    item.setValorUnitario(BigDecimal.valueOf(dto.getValor()));

                    item.setVenda(venda);

                    venda.getItens().add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public String editar(@PathVariable Long id, Model model) {

        Venda venda = vendaService.buscarPorId(id);

        model.addAttribute("clientes", clienteRepository.findByAtivoTrue());
        model.addAttribute("vendedores", usuarioRepository.findByAtivoTrue());
        model.addAttribute("statusList", StatusVenda.values());
        model.addAttribute("venda", venda);

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
