package com.marchioro.brewer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.marchioro.brewer.dto.MenuItem;

@Service
public class MenuService {

	public List<MenuItem> getMenu() {

	    Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();

	    if (auth == null || !auth.isAuthenticated()
	            || auth.getPrincipal().equals("anonymousUser")) {

	        return Collections.emptyList();
	    }

	    Set<String> permissoes = auth.getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toSet());

	    List<MenuItem> menu = new ArrayList<>();

	    // ========================
	    // CERVEJAS
	    // ========================
	    if (permissoes.contains("LISTAR_CERVEJA")
	            || permissoes.contains("CADASTRAR_CERVEJA")) {

	        MenuItem cervejas = new MenuItem("Cervejas", "#", "fa-beer");

	        if (permissoes.contains("CADASTRAR_CERVEJA")) {
	            cervejas.addFilho(new MenuItem("Cadastrar", "/cervejas/novo", "fa-plus"));
	        }

	        if (permissoes.contains("LISTAR_CERVEJA")) {
	            cervejas.addFilho(new MenuItem("Listar", "/cervejas", "fa-list"));
	        }
	        						
	        if (permissoes.contains("LISTAR_ESTILO")) {
	            cervejas.addFilho(new MenuItem("Estilos", "/estilos", "fa-list"));
	        }
	        	

	        menu.add(cervejas);
	    }

	    // ========================
	    // CLIENTES
	    // ========================
	    if (permissoes.contains("LISTAR_CLIENTE")
	            || permissoes.contains("CADASTRAR_CLIENTE")) {

	        MenuItem clientes = new MenuItem("Clientes", "#", "fa-users");

	        if (permissoes.contains("CADASTRAR_CLIENTE")) {
	            clientes.addFilho(new MenuItem("Cadastrar", "/clientes/novo", "fa-plus"));
	        }

	        if (permissoes.contains("LISTAR_CLIENTE")) {
	            clientes.addFilho(new MenuItem("Listar", "/clientes", "fa-list"));
	        }

	        menu.add(clientes);
	    }

	    // ========================
	    // USUÁRIOS
	    // ========================
	    if (permissoes.contains("LISTAR_USUARIO")
	            || permissoes.contains("CADASTRAR_USUARIO")) {

	        MenuItem usuarios = new MenuItem("Usuários", "#", "fa-user");

	        if (permissoes.contains("CADASTRAR_USUARIO")) {
	            usuarios.addFilho(new MenuItem("Cadastrar", "/usuario/novo", "fa-plus"));
	        }

	        if (permissoes.contains("LISTAR_USUARIO")) {
	            usuarios.addFilho(new MenuItem("Listar", "/usuario", "fa-list"));
	        }

	        menu.add(usuarios);
	    }
	    
	    // ========================
		 // VENDAS
		 // ========================
	    
	    if (permissoes.contains("LISTAR_VENDA")
	            || permissoes.contains("REALIZAR_VENDA")) {

	        MenuItem usuarios = new MenuItem("Vendas", "#", "fa-shopping-cart");

	        if (permissoes.contains("REALIZAR_VENDA")) {
	            usuarios.addFilho(new MenuItem("Realizar Venda", "/vendas/novo", "fa-plus"));
	        }

	        if (permissoes.contains("LISTAR_VENDA")) {
	            usuarios.addFilho(new MenuItem("Listar Vendas", "/vendas", "fa-list"));
	        }

	        menu.add(usuarios);
	    }
	    
	    
	 // ========================
	 // CONFIGURAÇÕES
	 // ========================
	 if (permissoes.contains("GERENCIAR_GRUPO")
	         || permissoes.contains("GERENCIAR_PERMISSAO")) {

	     MenuItem config = new MenuItem("Configurações", "#", "fa-cog");

	     // ========================
	     // GRUPOS
	     // ========================
	     if (permissoes.contains("GERENCIAR_GRUPO")) {

	         MenuItem grupos = new MenuItem("Grupos", "#", "fa-sitemap");

	         grupos.addFilho(new MenuItem("Listar Grupos", "/grupo", "fa-list"));
	         grupos.addFilho(new MenuItem("Cadastrar Grupo", "/grupo/novo", "fa-plus"));

	         config.addFilho(grupos);
	     }

	     // ========================
	     // PERMISSÕES
	     // ========================
	     if (permissoes.contains("GERENCIAR_PERMISSAO")) {

	         MenuItem permissoesMenu = new MenuItem("Permissões", "#", "fa-shield");

	         permissoesMenu.addFilho(new MenuItem("Listar", "/permissao", "fa-list"));
	         permissoesMenu.addFilho(new MenuItem("Nova", "/permissao/novo", "fa-plus"));

	         config.addFilho(permissoesMenu);
	     }

	     menu.add(config);
	 }
	    
	    return menu;
	}
}