package com.marchioro.brewer.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.marchioro.brewer.model.Usuario;

@Controller
public class PerfilController {
	
	@GetMapping("/perfil")
	public String perfil(Model model, Authentication authentication) {

	    Usuario usuario = (Usuario) authentication.getPrincipal();

	    model.addAttribute("usuario", usuario);
	    System.out.println("PRINCIPAL CLASS: " + authentication.getPrincipal().getClass());

	    return "perfil";
	    
	}
	
}