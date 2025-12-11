package com.marchioro.brewer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marchioro.brewer.model.Cerveja;
import com.marchioro.brewer.model.Origem;
import com.marchioro.brewer.model.Sabor;
import com.marchioro.brewer.repository.EstiloRepository;
import com.marchioro.brewer.service.CervejaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cervejas")  //  Prefixo para todas as rotas do controller
public class CervejasController {

    private static final Logger logger = LoggerFactory.getLogger(CervejasController.class);

    private final EstiloRepository estiloRepository;
    private final CervejaService cervejaService;

    // ------------------------------------------------------------
    //  Injeção de dependências pelo construtor
    // ------------------------------------------------------------
    public CervejasController(EstiloRepository estiloRepository, CervejaService cervejaService) {
        this.estiloRepository = estiloRepository;
        this.cervejaService = cervejaService;
    }

    // ------------------------------------------------------------
    //  Tela de cadastro (GET /cervejas/novo)
    // ------------------------------------------------------------
    @GetMapping("/novo")
    public String novo(Cerveja cerveja, Model model) {

        // Lista de enums para preencher selects e radios
        model.addAttribute("sabores", Sabor.values());
        model.addAttribute("origens", Origem.values());

        // ⚠ IMPORTANTE: O nome aqui deve ser "estilos", igual ao HTML
        model.addAttribute("estilos", estiloRepository.findAll());

        return "cerveja/CadastroCerveja"; // Template
    }

    // ------------------------------------------------------------
    //  Salvar cerveja no banco (POST /cervejas/novo)
    // ------------------------------------------------------------
    @PostMapping("/novo")
    public String salvar(
            @Valid Cerveja cerveja,   // valida a entidade
            BindingResult result,     // captura erros de validação
            Model model) {

        //  Se houver erros de validação, voltar para a mesma página
        if (result.hasErrors()) {

            // As listas PRECISAM ser carregadas novamente
            model.addAttribute("sabores", Sabor.values());
            model.addAttribute("origens", Origem.values());
            model.addAttribute("estilos", estiloRepository.findAll());

            return "cerveja/CadastroCerveja";
        }

        // Salva via service
        cervejaService.salvar(cerveja);

        logger.info("Cerveja salva com sucesso: SKU = {}", cerveja.getSku());

        // Redireciona para evitar reenvio do formulário
        return "redirect:/cervejas/novo?sucesso";
    }
}
