package com.marchioro.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClienteController {

	@RequestMapping("/cliente/novo")
	public String novo() {
		return "cliente/CadastroCliente";
	}
}
