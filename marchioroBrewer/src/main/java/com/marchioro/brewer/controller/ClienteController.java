package com.marchioro.brewer.controller;

import java.util.List;
import java.util.jar.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marchioro.brewer.model.Cliente;
import com.marchioro.brewer.model.Endereco;
import com.marchioro.brewer.model.Estado;
import com.marchioro.brewer.model.Regiao;
import com.marchioro.brewer.repository.CidadeRepository;
import com.marchioro.brewer.repository.ClienteRepository;
import com.marchioro.brewer.repository.EstadoRepository;
import com.marchioro.brewer.repository.RegiaoRepository;
import com.marchioro.brewer.service.ClienteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	 @Autowired
	private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;
    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;
    private final RegiaoRepository regiaoRepository;

    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository,
            CidadeRepository cidadeRepository,
            EstadoRepository estadoRepository,
            RegiaoRepository regiaoRepository) {
    	
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
        this.cidadeRepository = cidadeRepository;
        this.estadoRepository = estadoRepository;
        this.regiaoRepository = regiaoRepository;
    }

    // =========================
    // LISTAGEM
    // =========================
    // LISTAR + FILTRAR
    @GetMapping
    public ModelAndView listar(
            @RequestParam(required = false) String nomeCliente,
            @PageableDefault(size = 2) Pageable pageable) {

        Page<Cliente> page;

        if (nomeCliente != null && !nomeCliente.isEmpty()) {
            page = clienteRepository
                    .findByNomeClienteContainingIgnoreCaseAndAtivoTrue(nomeCliente, pageable);
        } else {
            page = clienteRepository.findByAtivoTrue(pageable);
        }

        ModelAndView mv = new ModelAndView("cliente/ListagemCliente");
        mv.addObject("page", page);
        mv.addObject("nomeCliente", nomeCliente);

        return mv;
    }


 // =========================
 // NOVO CLIENTE
 // =========================
 @GetMapping("/novo")
 public String novo(Model model) {

     Cliente cliente = new Cliente();
     cliente.setEndereco(new Endereco());

     model.addAttribute("cliente", cliente);

     // Carrega apenas regiões no início
     model.addAttribute("regioes", regiaoRepository.findByAtivoTrue());

     // IMPORTANTE:
     // Estados e cidades iniciam vazios
     // Serão carregados via JS conforme seleção
     model.addAttribute("estados", List.of());
     model.addAttribute("cidades", List.of());

     return "cliente/CadastroCliente";
 }



//=========================
// SALVAR CLIENTE
// =========================
 @PostMapping("/salvar")
 public String salvar(
         @Valid Cliente cliente,
         BindingResult result,
         Model model,
         RedirectAttributes attributes) {

     if (result.hasErrors()) {

         // Sempre carrega regiões
         model.addAttribute("regioes", regiaoRepository.findByAtivoTrue());

         // Se já existe estado, carrega estados da região dele
         if (cliente.getEndereco() != null &&
             cliente.getEndereco().getCidade() != null &&
             cliente.getEndereco().getCidade().getEstado() != null) {

             Estado estado = cliente.getEndereco().getCidade().getEstado();
             Regiao regiao = estado.getRegiao();

             model.addAttribute(
                 "estados",
                 estadoRepository.findByRegiaoIdAndAtivoTrue(regiao.getId())
             );

             model.addAttribute(
                 "cidades",
                 cidadeRepository.findByEstadoIdAndAtivoTrue(estado.getId())
             );

         } else {
             // fallback seguro
             model.addAttribute("estados", List.of());
             model.addAttribute("cidades", List.of());
         }

         return "cliente/CadastroCliente";
     }

     // =====================================================
     // REMOVE MÁSCARAS (salva somente números no banco)
     // =====================================================
     if (cliente.getDocumento() != null) {
         cliente.setDocumento(
             cliente.getDocumento().replaceAll("\\D", "")
         );
     }

     if (cliente.getTelefone() != null) {
         cliente.setTelefone(
             cliente.getTelefone().replaceAll("\\D", "")
         );
     }

     clienteRepository.save(cliente);
     
     attributes.addFlashAttribute("mensagemSucesso", "Cliente salvo com sucesso...!");

     return "redirect:/clientes/novo";
 }





//=========================
//EDITAR CLIENTE
//=========================
 @GetMapping("/editar/{id}")
 public String editar(@PathVariable Long id, Model model) {

     Cliente cliente = clienteRepository.findById(id)
             .orElseThrow(() -> new IllegalArgumentException("Cliente inválido"));

     model.addAttribute("cliente", cliente);

     // Carrega todas as regiões
     model.addAttribute("regioes", regiaoRepository.findByAtivoTrue());

     // Recupera dados encadeados
     Regiao regiaoSelecionada =
             cliente.getEndereco().getCidade().getEstado().getRegiao();

     Estado estadoSelecionado =
             cliente.getEndereco().getCidade().getEstado();

     // Carrega estados da região
     model.addAttribute("estados",
             estadoRepository.findByRegiaoIdAndAtivoTrue(regiaoSelecionada.getId()));

     // Carrega cidades do estado
     model.addAttribute("cidades",
             cidadeRepository.findByEstadoIdAndAtivoTrue(estadoSelecionado.getId()));

     return "cliente/CadastroCliente";
 }
 
 
//SOFT DELETE
 @PostMapping("/excluir/{id}")
 public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
     Cliente cliente = clienteRepository.findById(id)
             .orElseThrow(() -> new IllegalArgumentException("Cliente inválido"));

     cliente.setAtivo(false);
     clienteRepository.save(cliente);
     
     attributes.addFlashAttribute("mensagemSucesso", "Cliente excluído com sicesso...!");

     return "redirect:/clientes";
 }

}
