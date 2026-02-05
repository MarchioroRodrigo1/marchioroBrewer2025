package com.marchioro.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marchioro.brewer.model.Cidade;
import com.marchioro.brewer.model.Cliente;
import com.marchioro.brewer.model.Endereco;
import com.marchioro.brewer.repository.CidadeRepository;
import com.marchioro.brewer.repository.ClienteRepository;
import com.marchioro.brewer.repository.EstadoRepository;
import com.marchioro.brewer.repository.RegiaoRepository;
import com.marchioro.brewer.service.ClienteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;
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
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarAtivos());
        return "cliente/ListarClientes";
    }

    // =========================
    // NOVO CLIENTE
    // =========================
    @GetMapping("/novo")
    public String novo(Model model) {

        Cliente cliente = new Cliente();
        cliente.setEndereco(new Endereco());

        model.addAttribute("cliente", cliente);
        model.addAttribute("regioes", regiaoRepository.findAll());
        model.addAttribute("estados", estadoRepository.findAll());
        model.addAttribute("cidades", cidadeRepository.findByAtivoTrue());

        return "cliente/CadastroCliente";
    }



    // =========================
    // SALVAR CLIENTE
    // =========================
    @PostMapping("/salvar")
    public String salvar(
            @Valid Cliente cliente,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("regioes", regiaoRepository.findAll());
            model.addAttribute("estados", estadoRepository.findAll());
            model.addAttribute("cidades", cidadeRepository.findByAtivoTrue());

            return "cliente/CadastroCliente";
        }

        clienteRepository.save(cliente);
        return "redirect:/clientes/novo";
    }



    // =========================
    // EDITAR CLIENTE
    // =========================
    @GetMapping("/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("cliente", cliente);

        return "cliente/CadastroCliente";
    }

    // =========================
    // EXCLUSÃO LÓGICA
    // =========================
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes attributes) {

        clienteService.excluir(id);

        attributes.addFlashAttribute(
            "mensagem",
            "Cliente excluído com sucesso!"
        );

        return "redirect:/clientes";
    }
}
