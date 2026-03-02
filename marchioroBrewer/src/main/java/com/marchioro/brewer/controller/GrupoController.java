package com.marchioro.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marchioro.brewer.model.Grupo;
import com.marchioro.brewer.repository.PermissaoRepository;
import com.marchioro.brewer.service.GrupoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/grupo")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private PermissaoRepository permissoes;

    // =================================
    // NOVO
    // =================================
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/novo")
    public ModelAndView novo(Grupo grupo) {

        ModelAndView mv =
                new ModelAndView("grupo/CadastroGrupo");

        mv.addObject("permissoes", permissoes.findAll());

        return mv;
    }

    // =================================
    // SALVAR
    // =================================
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/novo")
    public ModelAndView salvar(
            @Valid Grupo grupo,
            BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            ModelAndView mv =
                    new ModelAndView("grupo/CadastroGrupo");

            mv.addObject("permissoes", permissoes.findAll());
            return mv;
        }

        try {
            grupoService.salvar(grupo);

        } catch (IllegalArgumentException e) {

            result.rejectValue(
                    "nomeGrupo",
                    "erro.nome",
                    e.getMessage());

            ModelAndView mv =
                    new ModelAndView("grupo/CadastroGrupo");

            mv.addObject("permissoes", permissoes.findAll());
            return mv;
        }

        attributes.addFlashAttribute(
                "mensagem",
                "Grupo salvo com sucesso!");

        return new ModelAndView("redirect:/grupo");
    }

    // =================================
    // LISTAR
    // =================================
    @GetMapping
    public ModelAndView listar() {

        ModelAndView mv =
                new ModelAndView("grupo/ListagemGrupos");

        mv.addObject("grupos",
                grupoService.listarAtivos());

        return mv;
    }

    // =================================
    // EDITAR
    // =================================
    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable Long id) {

        Grupo grupo = grupoService.buscar(id);

        ModelAndView mv =
                new ModelAndView("grupo/CadastroGrupo");

        mv.addObject("grupo", grupo);
        mv.addObject("permissoes", permissoes.findAll());

        return mv;
    }

    // =================================
    // EXCLUIR (SOFT DELETE)
    // =================================
    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes attributes) {

        grupoService.desativar(id);

        attributes.addFlashAttribute(
                "mensagemSucesso",
                "Grupo desativado com sucesso!");

        return "redirect:/grupo";
    }

}
