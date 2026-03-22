package com.marchioro.brewer.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marchioro.brewer.dto.CervejaFiltro;
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
        model.addAttribute("estilos", estiloRepository.findByAtivoTrue());


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
            RedirectAttributes attributes,
            @RequestParam("arquivo") MultipartFile arquivo) throws IOException {

        // Se houver erro de validação, retorna para o formulário
        if (result.hasErrors()) {

            model.addAttribute("sabores", Sabor.values());
            model.addAttribute("origens", Origem.values());
            model.addAttribute("estilos", estiloRepository.findByAtivoTrue());

            return "cerveja/CadastroCerveja";
        }

        //  Recupera imagem antiga (caso edição)
        String imagemAtual = null;

        if (cerveja.getId() != null) {
            Cerveja cervejaSalva = cervejaService.buscarPorCodigo(cerveja.getId());
            imagemAtual = cervejaSalva.getUrlImagem();
        }

        //  Upload da nova imagem (se houver)
        if (!arquivo.isEmpty()) {

            String nomeArquivo = UUID.randomUUID() + "-" + arquivo.getOriginalFilename();

            Path pasta = Paths.get("uploads/cervejas");
            Files.createDirectories(pasta);

            Path caminho = pasta.resolve(nomeArquivo);

            //  REDIMENSIONA E SALVA (AQUI ESTÁ A MÁGICA)
            net.coobird.thumbnailator.Thumbnails.of(arquivo.getInputStream())
                    .size(300, 300)            // tamanho máximo
                    .keepAspectRatio(true)     // mantém proporção
                    .outputQuality(0.8)        // compressão (0.0 a 1.0)
                    .toFile(caminho.toFile());

            cerveja.setUrlImagem(nomeArquivo);

            //  Remove imagem antiga
            if (imagemAtual != null) {
                Path antiga = Paths.get("uploads/cervejas/" + imagemAtual);
                Files.deleteIfExists(antiga);
            }

        } else {
            //  Mantém imagem antiga se não enviou nova
            cerveja.setUrlImagem(imagemAtual);
        }

        // Persiste a cerveja
        cervejaService.salvar(cerveja);

        logger.info("Cerveja salva com sucesso: SKU = {}", cerveja.getSku());

        attributes.addFlashAttribute(
                "mensagemSucesso",
                "Cerveja cadastrada com sucesso!"
        );

        return "redirect:/cervejas";
    }

 // ------------------------------------------------------------
//  Listagem de cervejas (GET /cervejas)
// ------------------------------------------------------------
@GetMapping
public String listar(
        CervejaFiltro filtro,               // DTO de filtro
        @PageableDefault(size = 5) Pageable pageable,
        Model model) {

    // Busca paginada usando o filtro
    Page<Cerveja> page = cervejaService.filtrar(filtro, pageable);

    // Dados para a tela
    model.addAttribute("page", page);
    model.addAttribute("cervejas", page.getContent());
    model.addAttribute("filtro", filtro);
    model.addAttribute("estilos", estiloRepository.findByAtivoTrue());

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
        model.addAttribute("estilos", estiloRepository.findByAtivoTrue());

        // Reaproveita o formulário de cadastro
        model.addAttribute("cerveja", cerveja);

        return "cerveja/CadastroCerveja";
    }
    
 // ------------------------------------------------------------
 // Exclusão lógica de cerveja (Soft Delete)
 // ------------------------------------------------------------
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        cervejaService.excluir(id);

        return ResponseEntity.ok().build();
    }
    
  
    @GetMapping("/teste-permissoes")
    @ResponseBody
    public String testePermissoes(Authentication authentication) {

        StringBuilder sb = new StringBuilder();

        authentication.getAuthorities()
                .forEach(a -> sb.append(a.getAuthority()).append("<br>"));

        return sb.toString();

    }
    
    // Imagem
    
    @GetMapping("/imagem/{nome}")
    @ResponseBody
    public ResponseEntity<Resource> imagem(@PathVariable String nome) throws IOException {

        Path path = Paths.get("uploads/cervejas/" + nome);
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resource);
    }
    
}
