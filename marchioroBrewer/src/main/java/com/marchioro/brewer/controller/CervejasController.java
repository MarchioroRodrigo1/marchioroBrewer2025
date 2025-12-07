package com.marchioro.brewer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping; // Import para GET
import org.springframework.web.bind.annotation.PostMapping; // Import para POST
import org.springframework.web.bind.annotation.RequestMapping; // Import para mapeamento de classe
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marchioro.brewer.model.Cerveja;
import jakarta.validation.Valid;

@Controller
// 1. Adicionado: Define o prefixo base para todos os métodos da classe
@RequestMapping("/cervejas")
public class CervejasController {
	
	private static final Logger logger = LoggerFactory.getLogger(CervejasController.class);

	// 2. Simplificado: Usando @GetMapping para a URL /cervejas/novo
	@GetMapping("/novo") 
	public String novo(Cerveja cerveja) {
		return "cerveja/CadastroCerveja";
	}
	
	// 3. Simplificado: Usando @PostMapping para a URL /cervejas/novo
	@PostMapping("/novo")
	public String cadastrar(@Valid Cerveja cerveja, BindingResult result, Model model, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			return novo(cerveja);
		}
		
		// *Aqui você deve inserir a lógica para salvar a cerveja no banco de dados*
		
		attributes.addFlashAttribute("mensagem", "Cerveja foi cadastrada com sucesso!");
		logger.info("SKU: " + cerveja.getSku());
		
		// 4. Corrigido: O redirecionamento aponta para o mapeamento da classe
		return "redirect:/cervejas/novo"; 
	}
	
}