package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.Categoria;
import br.com.atelieritalara.backend.entity.Produto;
import br.com.atelieritalara.backend.repository.CategoriaRepository;
import br.com.atelieritalara.backend.service.ProdutoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaRepository categoriaRepository;

    public ProdutoController(
            ProdutoService produtoService,
            CategoriaRepository categoriaRepository
    ) {
        this.produtoService = produtoService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Produto produto) {

        if (produto.getCategoria() == null
                || produto.getCategoria().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("categoria.id é obrigatório");
        }

        return categoriaRepository.findById(produto.getCategoria().getId())
                .map(categoria -> {
                    produto.setCategoria(categoria);

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(produtoService.salvar(produto));
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Produto produto
    ) {
        return produtoService.buscarPorId(id)
                .map(produtoExistente -> {

                    if (produto.getCategoria() == null
                            || produto.getCategoria().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("categoria.id é obrigatório");
                    }

                    return categoriaRepository
                            .findById(produto.getCategoria().getId())
                            .map(categoria -> {

                                produtoExistente.setCategoria(categoria);
                                produtoExistente.setNome(produto.getNome());
                                produtoExistente.setDescricao(produto.getDescricao());
                                produtoExistente.setAtivo(produto.getAtivo());
                                produtoExistente.setDestaque(produto.getDestaque());

                                return ResponseEntity.ok(
                                        produtoService.salvar(produtoExistente)
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

        if (produtoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produtoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}