package com.marchioro.brewer.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import com.marchioro.brewer.model.OnCreate;
import com.marchioro.brewer.model.Usuario;
import com.marchioro.brewer.repository.GrupoRepository;
import com.marchioro.brewer.repository.UsuarioRepository;
import com.marchioro.brewer.service.UsuarioService;

import jakarta.validation.ConstraintViolation;

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
    private GrupoRepository grupos;


    @Autowired
    private UsuarioRepository usuarioRepository;
    // =====================================================
    // ABRIR FORM NOVO USUÁRIO
    // =====================================================
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_USUARIO')")
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
        //  VALIDAÇÃO POR GRUPO (CREATE / UPDATE)
        // =====================================================
        Set<ConstraintViolation<Usuario>> violations;

        if (usuario.getId() == null) {
            // CREATE → valida grupo OnCreate
            violations = validator.validate(usuario, OnCreate.class);
        } else {
            // UPDATE → valida padrão
            violations = validator.validate(usuario);
        }

        for (ConstraintViolation<Usuario> v : violations) {

            // ignora senha vazia durante edição
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
        //  VALIDAR EMAIL DUPLICADO (CORRIGIDO)
        // =====================================================
        Optional<Usuario> usuarioExistente =
                usuarioRepository.findByEmailUsuario(usuario.getEmailUsuario());

        if (usuarioExistente.isPresent()) {

            Usuario existente = usuarioExistente.get();

            // permite salvar se for o mesmo usuário (edição)
            if (usuario.getId() == null ||
                !existente.getId().equals(usuario.getId())) {

                result.rejectValue(
                        "emailUsuario",
                        "erro.email",
                        "Já existe usuário cadastrado com este e-mail");
            }
        }

        // =====================================================
        //  CONFIRMAÇÃO DE SENHA (somente se digitada)
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
        // 4️⃣ TRATAMENTO CORRETO DA SENHA
        // =====================================================
        if (usuario.getId() != null) {

            // ===== EDIÇÃO =====
            Usuario usuarioBanco =
                    usuarioRepository.findById(usuario.getId()).orElseThrow();

            if (usuario.getSenhaUsuario() == null ||
                usuario.getSenhaUsuario().isBlank()) {

                // mantém senha antiga
                usuario.setSenhaUsuario(
                        usuarioBanco.getSenhaUsuario());

            } else {

                // criptografa nova senha
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
        usuarioRepository.save(usuario);

        attributes.addFlashAttribute(
                "mensagemSucesso",
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
                    usuarioRepository.findByNomeUsuarioContainingIgnoreCaseAndAtivoTrue(nomeUsuario));

        } else {

            mv.addObject(
                    "usuarios",
                    usuarioRepository.findByAtivoTrue());
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
                usuarioRepository.findById(id)
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
                usuarioRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Usuário inválido"));

        usuario.setAtivo(false);

        usuarioRepository.save(usuario);

        attributes.addFlashAttribute(
                "mensagemSucesso",
                "Usuário desativado com sucesso!");

        return "redirect:/usuario";
    }

    // =====================================================
    // LISTAGEM PAGINADA (SERVICE)
    // =====================================================
    @GetMapping("/usuario")
    public String listarUsuarios(
            @RequestParam(required = false) String nomeUsuario,
            @PageableDefault(size = 5) Pageable pageable,
            Model model) {

        Page<Usuario> page = usuarioService.listar(nomeUsuario, pageable);

        model.addAttribute("page", page);
        model.addAttribute("usuarios", page.getContent());
        model.addAttribute("nomeUsuario", nomeUsuario);

        return "usuario/listagemUsuario";
    }
    
    
    @GetMapping("/avatar/{id}")
    @ResponseBody
    public ResponseEntity<Resource> avatar(@PathVariable Long id) throws IOException {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        if (usuario == null || usuario.getUrlAvatar() == null) {
            Path path = Paths.get("src/main/resources/static/images/avatar-default.png");
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok(resource);
        }

        Path path = Paths.get("uploads/avatars/" + usuario.getUrlAvatar());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok(resource);
    }
    
    //uploadAvatar
    @PostMapping("/avatar/upload")
    public String uploadAvatar(
            @RequestParam("arquivo") MultipartFile arquivo,
            Authentication authentication,
            RedirectAttributes attributes) throws IOException {

        if (arquivo.isEmpty()) {
            attributes.addFlashAttribute("mensagemErro", "Selecione um arquivo!");
            return "redirect:/usuario";
        }

        // pega usuário logado
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmailUsuario(email).orElseThrow();

        // gera nome único
        String nomeArquivo = usuario.getId() + "_" + arquivo.getOriginalFilename();

        // caminho onde será salvo
        Path caminho = Paths.get("uploads/avatars/" + nomeArquivo);

        // cria pasta se não existir
        Files.createDirectories(caminho.getParent());

        // salva arquivo
        Files.copy(arquivo.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

        // salva no banco
        usuario.setUrlAvatar(nomeArquivo);
        usuarioRepository.save(usuario);

        attributes.addFlashAttribute("mensagemSucesso", "Avatar atualizado!");

        return "redirect:/usuario";
    }
    
}