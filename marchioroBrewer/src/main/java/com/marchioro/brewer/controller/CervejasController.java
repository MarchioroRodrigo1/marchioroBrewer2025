package com.marchioro.brewer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marchioro.brewer.model.Cerveja;
import com.marchioro.brewer.model.Origem;
import com.marchioro.brewer.model.Sabor;
import com.marchioro.brewer.repository.EstiloRepository;
import com.marchioro.brewer.service.CervejaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cervejas") // Prefixo base do controller
public class CervejasController {

    private static final Logger logger =
            LoggerFactory.getLogger(CervejasController.class);

    private final EstiloRepository estiloRepository;
    private final CervejaService cervejaService;

    // ------------------------------------------------------------
    // Injeção de dependências via construtor
    // ------------------------------------------------------------
    public CervejasController(EstiloRepository estiloRepository,
                              CervejaService cervejaService) {
        this.estiloRepository = estiloRepository;
        this.cervejaService = cervejaService;
    }

    // ------------------------------------------------------------
    // Tela de cadastro (GET /cervejas/novo)
    // ------------------------------------------------------------
    @GetMapping("/novo")
    public String novo(Cerveja cerveja, Model model) {

        // Enums usados no formulário
        model.addAttribute("sabores", Sabor.values());
        model.addAttribute("origens", Origem.values());

        // Lista de estilos (select)
        model.addAttribute("estilos", estiloRepository.findAll());

        return "cerveja/CadastroCerveja";
    }

    // ------------------------------------------------------------
    // Salvar cerveja (POST /cervejas/novo)
    // ------------------------------------------------------------
    @PostMapping("/novo")
    public String salvar(
            @Valid Cerveja cerveja,
            BindingResult result,
            Model model,
            RedirectAttributes attributes) {

        // Se houver erro de validação, retorna para o formulário
        if (result.hasErrors()) {

            model.addAttribute("sabores", Sabor.values());
            model.addAttribute("origens", Origem.values());
            model.addAttribute("estilos", estiloRepository.findAll());

            return "cerveja/CadastroCerveja";
        }

        // Persiste a cerveja
        cervejaService.salvar(cerveja);

        logger.info("Cerveja salva com sucesso: SKU = {}", cerveja.getSku());

        // Mensagem global exibida após redirect
        attributes.addFlashAttribute(
                "mensagemSucesso",
                "Cerveja cadastrada com sucesso!"
        );

        return "redirect:/cervejas";
    }

    // ------------------------------------------------------------
    // Listagem de cervejas (GET /cervejas)
    // ------------------------------------------------------------
    @GetMapping
    public String listar(
            @RequestParam(required = false, defaultValue = "") String nome,
            @RequestParam(required = false) Long estilo,
            @PageableDefault(size = 5) Pageable pageable,
            Model model) {

        // Delegamos TODA a lógica de filtro para o service
        Page<Cerveja> page = cervejaService.filtrar(nome, estilo, pageable);

        // ISSO ERA O PRINCIPAL MOTIVO DE NÃO LISTAR
        model.addAttribute("cervejas", page.getContent());

        // Usado para paginação
        model.addAttribute("page", page);

        // Mantém valores do filtro no formulário
        model.addAttribute("nome", nome);
        model.addAttribute("estilo", estilo);

        // Lista de estilos para o select
        model.addAttribute("estilos", estiloRepository.findAll());

        return "cerveja/ListarCervejas";
    }

    // ------------------------------------------------------------
    // Edição de cerveja (GET /cervejas/{codigo})
    // ------------------------------------------------------------
    @GetMapping("/{codigo}")
    public String editar(@PathVariable Long codigo, Model model) {

        // Busca a cerveja pelo ID
        Cerveja cerveja = cervejaService.buscarPorCodigo(codigo);

        // Dados auxiliares do formulário
        model.addAttribute("sabores", Sabor.values());
        model.addAttribute("origens", Origem.values());
        model.addAttribute("estilos", estiloRepository.findAll());

        // Reaproveita o formulário de cadastro
        model.addAttribute("cerveja", cerveja);

        return "cerveja/CadastroCerveja";
    }
}
