package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.ProdutoOpcao;
import br.com.atelieritalara.backend.repository.ProdutoRepository;
import br.com.atelieritalara.backend.service.ProdutoOpcaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto-opcoes")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoOpcaoController {

    private final ProdutoOpcaoService produtoOpcaoService;
    private final ProdutoRepository produtoRepository;

    public ProdutoOpcaoController(
            ProdutoOpcaoService produtoOpcaoService,
            ProdutoRepository produtoRepository
    ) {
        this.produtoOpcaoService = produtoOpcaoService;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoOpcao>> listar() {
        return ResponseEntity.ok(produtoOpcaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoOpcao> buscarPorId(@PathVariable Long id) {
        return produtoOpcaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ProdutoOpcao produtoOpcao) {

        if (produtoOpcao.getProduto() == null
                || produtoOpcao.getProduto().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produto.id é obrigatório");
        }

        return produtoRepository.findById(produtoOpcao.getProduto().getId())
                .map(produto -> {
                    produtoOpcao.setProduto(produto);

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(produtoOpcaoService.salvar(produtoOpcao));
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoOpcao produtoOpcao
    ) {
        return produtoOpcaoService.buscarPorId(id)
                .map(opcaoExistente -> {

                    if (produtoOpcao.getProduto() == null
                            || produtoOpcao.getProduto().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produto.id é obrigatório");
                    }

                    return produtoRepository.findById(
                                    produtoOpcao.getProduto().getId()
                            )
                            .map(produto -> {

                                opcaoExistente.setProduto(produto);
                                opcaoExistente.setNome(produtoOpcao.getNome());
                                opcaoExistente.setTipo(produtoOpcao.getTipo());
                                opcaoExistente.setObrigatorio(
                                        produtoOpcao.getObrigatorio()
                                );
                                opcaoExistente.setOrdem(
                                        produtoOpcao.getOrdem()
                                );

                                return ResponseEntity.ok(
                                        produtoOpcaoService.salvar(opcaoExistente)
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

        if (produtoOpcaoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produtoOpcaoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}