package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.ProdutoVariacao;
import br.com.atelieritalara.backend.repository.ProdutoRepository;
import br.com.atelieritalara.backend.service.ProdutoVariacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto-variacoes")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoVariacaoController {

    private final ProdutoVariacaoService produtoVariacaoService;
    private final ProdutoRepository produtoRepository;

    public ProdutoVariacaoController(
            ProdutoVariacaoService produtoVariacaoService,
            ProdutoRepository produtoRepository
    ) {
        this.produtoVariacaoService = produtoVariacaoService;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoVariacao>> listar() {
        return ResponseEntity.ok(produtoVariacaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoVariacao> buscarPorId(@PathVariable Long id) {
        return produtoVariacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ProdutoVariacao produtoVariacao) {

        if (produtoVariacao.getProduto() == null
                || produtoVariacao.getProduto().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produto.id é obrigatório");
        }

        return produtoRepository.findById(
                        produtoVariacao.getProduto().getId()
                )
                .map(produto -> {
                    produtoVariacao.setProduto(produto);

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(produtoVariacaoService.salvar(produtoVariacao));
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoVariacao produtoVariacao
    ) {
        return produtoVariacaoService.buscarPorId(id)
                .map(variacaoExistente -> {

                    if (produtoVariacao.getProduto() == null
                            || produtoVariacao.getProduto().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produto.id é obrigatório");
                    }

                    return produtoRepository.findById(
                                    produtoVariacao.getProduto().getId()
                            )
                            .map(produto -> {

                                variacaoExistente.setProduto(produto);
                                variacaoExistente.setNome(produtoVariacao.getNome());
                                variacaoExistente.setPreco(produtoVariacao.getPreco());
                                variacaoExistente.setDiametroCm(produtoVariacao.getDiametroCm());
                                variacaoExistente.setQuantidadeFatias(
                                        produtoVariacao.getQuantidadeFatias()
                                );
                                variacaoExistente.setQuantidadeUnidades(
                                        produtoVariacao.getQuantidadeUnidades()
                                );
                                variacaoExistente.setAtivo(produtoVariacao.getAtivo());
                                variacaoExistente.setOrdem(produtoVariacao.getOrdem());

                                return ResponseEntity.ok(
                                        produtoVariacaoService.salvar(variacaoExistente)
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

        if (produtoVariacaoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produtoVariacaoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}