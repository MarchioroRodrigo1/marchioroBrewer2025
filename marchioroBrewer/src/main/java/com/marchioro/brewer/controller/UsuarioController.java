package com.marchioro.brewer.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marchioro.brewer.model.OnCreate;
import com.marchioro.brewer.model.OnUpdate;
import com.marchioro.brewer.model.Usuario;
import com.marchioro.brewer.repository.GrupoRepository;
import com.marchioro.brewer.repository.UsuarioRepository;
import com.marchioro.brewer.service.UsuarioService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private jakarta.validation.Validator validator;
	
	@Autowired
	private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarios;

    @Autowired
    private GrupoRepository grupos;

    // =====================================================
    // ABRIR FORM NOVO USUÁRIO
    // =====================================================
    @GetMapping("/novo")
    public ModelAndView novo(Usuario usuario) {

        ModelAndView mv =
                new ModelAndView("usuario/CadastroUsuario");

        mv.addObject("grupos", grupos.findAll());

        return mv;
    }
 // =====================================================
 // SALVAR / EDITAR USUÁRIO
 // =====================================================
 @PostMapping("/novo")
 public ModelAndView salvar(
         Usuario usuario,
         BindingResult result,
         RedirectAttributes attributes) {

     // =====================================================
     // >>> AJUSTE 1
     // Validação por grupo (CREATE / UPDATE)
     // IGNORA senha vazia na edição
     // =====================================================
     Set<ConstraintViolation<Usuario>> violations;

     if (usuario.getId() == null) {
         // CREATE → valida tudo
         violations = validator.validate(usuario, OnCreate.class);
     } else {
         // UPDATE → valida default
         violations = validator.validate(usuario);
     }

     for (ConstraintViolation<Usuario> v : violations) {

         // >>> AJUSTE 2
         // evita erro de senha quando estiver vazia na edição
         if ("senhaUsuario".equals(v.getPropertyPath().toString())
                 && usuario.getId() != null
                 && (usuario.getSenhaUsuario() == null
                 || usuario.getSenhaUsuario().isBlank())) {
             continue;
         }

         result.rejectValue(
                 v.getPropertyPath().toString(),
                 "",
                 v.getMessage());
     }

     // =====================================================
     // VALIDAR EMAIL DUPLICADO
     // =====================================================
     Usuario usuarioExistente =
             usuarios.findByEmailUsuario(usuario.getEmailUsuario());

     if (usuarioExistente != null &&
         (usuario.getId() == null ||
          !usuarioExistente.getId().equals(usuario.getId()))) {

         result.rejectValue(
                 "emailUsuario",
                 "erro.email",
                 "Já existe usuário cadastrado com este e-mail");
     }

     // =====================================================
     // >>> AJUSTE 3
     // confirmação senha somente se digitada
     // =====================================================
     if (usuario.getSenhaUsuario() != null &&
         !usuario.getSenhaUsuario().isBlank()) {

         if (!usuario.getSenhaUsuario()
                 .equals(usuario.getConfirmacaoSenha())) {

             result.rejectValue(
                     "confirmacaoSenha",
                     "erro.confirmacao",
                     "As senhas não conferem");
         }
     }

     // =====================================================
     // SE EXISTIR ERRO → VOLTA FORM
     // =====================================================
     if (result.hasErrors()) {

         ModelAndView mv =
                 new ModelAndView("usuario/CadastroUsuario");

         mv.addObject("grupos", grupos.findAll());
         return mv;
     }

     // =====================================================
     // >>> AJUSTE 4 (MAIS IMPORTANTE)
     // TRATAMENTO CORRETO DA SENHA
     // =====================================================

     if (usuario.getId() != null) {

         // ===== EDIÇÃO =====
         Usuario usuarioBanco =
                 usuarios.findById(usuario.getId()).orElseThrow();

         if (usuario.getSenhaUsuario() == null ||
             usuario.getSenhaUsuario().isBlank()) {

             // mantém senha antiga
             usuario.setSenhaUsuario(
                     usuarioBanco.getSenhaUsuario());

         } else {

             // nova senha → criptografa
             usuario.setSenhaUsuario(
                     passwordEncoder.encode(usuario.getSenhaUsuario()));
         }

     } else {

         // ===== CRIAÇÃO =====
         usuario.setSenhaUsuario(
                 passwordEncoder.encode(usuario.getSenhaUsuario()));
     }

     // =====================================================
     // SALVAR
     // =====================================================
     usuarios.save(usuario);

     attributes.addFlashAttribute(
             "mensagem",
             "Usuário salvo com sucesso!");

     return new ModelAndView("redirect:/usuario");
 }

    // =====================================================
    // LISTAGEM + FILTRO
    // =====================================================
    @GetMapping
    public ModelAndView listar(
            @RequestParam(required = false) String nomeUsuario) {

        ModelAndView mv =
                new ModelAndView("usuario/ListagemUsuarios");

        if (nomeUsuario != null && !nomeUsuario.isBlank()) {

            mv.addObject(
                    "usuarios",
                    usuarios
                        .findByNomeUsuarioContainingIgnoreCaseAndAtivoTrue(nomeUsuario));

        } else {

            mv.addObject(
                    "usuarios",
                    usuarios.findByAtivoTrue());
        }

        mv.addObject("nomeUsuario", nomeUsuario);

        return mv;
    }

    // =====================================================
    // EDITAR
    // =====================================================
    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable Long id) {

        Usuario usuario =
                usuarios.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Usuário inválido"));

        ModelAndView mv =
                new ModelAndView("usuario/CadastroUsuario");

        mv.addObject("usuario", usuario);
        mv.addObject("grupos", grupos.findAll());

        return mv;
    }

    // =====================================================
    // SOFT DELETE (DESATIVAR)
    // =====================================================
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes attributes) {

        Usuario usuario =
                usuarios.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Usuário inválido"));

        usuario.setAtivo(false);

        usuarios.save(usuario);

        attributes.addFlashAttribute(
                "mensagemSucesso",
                "Usuário desativado com sucesso!");

        return "redirect:/usuario";
    }
    
    @GetMapping("/usuario")
    public String listarUsuarios(
            @RequestParam(required = false) String nomeUsuario,
            @PageableDefault(size = 5) Pageable pageable,
            Model model) {

        Page<Usuario> page = usuarioService.listar(nomeUsuario, pageable);

        model.addAttribute("page", page);              // ✅ IMPORTANTE
        model.addAttribute("usuarios", page.getContent());
        model.addAttribute("nomeUsuario", nomeUsuario);

        return "usuario/listagemUsuario";
    }
    
}