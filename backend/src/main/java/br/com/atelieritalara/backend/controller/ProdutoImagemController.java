package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.ProdutoImagem;
import br.com.atelieritalara.backend.repository.ProdutoRepository;
import br.com.atelieritalara.backend.service.ProdutoImagemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto-imagens")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoImagemController {

    private final ProdutoImagemService produtoImagemService;
    private final ProdutoRepository produtoRepository;

    public ProdutoImagemController(
            ProdutoImagemService produtoImagemService,
            ProdutoRepository produtoRepository
    ) {
        this.produtoImagemService = produtoImagemService;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoImagem>> listar() {
        return ResponseEntity.ok(produtoImagemService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoImagem> buscarPorId(@PathVariable Long id) {
        return produtoImagemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ProdutoImagem produtoImagem) {

        if (produtoImagem.getProduto() == null
                || produtoImagem.getProduto().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produto.id é obrigatório");
        }

        return produtoRepository.findById(produtoImagem.getProduto().getId())
                .map(produto -> {
                    produtoImagem.setProduto(produto);

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(produtoImagemService.salvar(produtoImagem));
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoImagem produtoImagem
    ) {
        return produtoImagemService.buscarPorId(id)
                .map(imagemExistente -> {

                    if (produtoImagem.getProduto() == null
                            || produtoImagem.getProduto().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produto.id é obrigatório");
                    }

                    return produtoRepository
                            .findById(produtoImagem.getProduto().getId())
                            .map(produto -> {

                                imagemExistente.setProduto(produto);
                                imagemExistente.setUrl(produtoImagem.getUrl());
                                imagemExistente.setPrincipal(produtoImagem.getPrincipal());
                                imagemExistente.setOrdem(produtoImagem.getOrdem());

                                return ResponseEntity.ok(
                                        produtoImagemService.salvar(imagemExistente)
                                );
                            })
                            .orElseGet(() ->
                                    ResponseEntity.notFound().build()
                            );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (produtoImagemService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produtoImagemService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}