package com.marchioro.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marchioro.brewer.model.Permissao;
import com.marchioro.brewer.repository.PermissaoRepository;
import com.marchioro.brewer.service.PermissaoService;

@Controller
@RequestMapping("/permissao")
public class PermissaoController {
	
	 private final PermissaoRepository repository;
	    private final PermissaoService service;

	    public PermissaoController(PermissaoRepository repository,
	                               PermissaoService service) {
	        this.repository = repository;
	        this.service = service;
	    }

	    @GetMapping("/novo")
	    public String novo(Model model) {
	        model.addAttribute("permissao", new Permissao());
	        return "permissao/CadastroPermissao";
	    }

	    @PostMapping
	    public String salvar(Permissao permissao, Model model) {

	        try {
	            service.salvar(permissao);
	            return "redirect:/permissao";
	        } catch (IllegalArgumentException e) {
	            model.addAttribute("erro", e.getMessage());
	            return "permissao/CadastroPermissao";
	        }
	    }

	    @GetMapping
	    public String listar(Model model) {
	        model.addAttribute("permissao", repository.findByAtivoTrue());
	        return "permissao/ListagemPermissao";
	    }

}
