package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.ProdutoOpcaoValor;
import br.com.atelieritalara.backend.repository.ProdutoOpcaoRepository;
import br.com.atelieritalara.backend.service.ProdutoOpcaoValorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto-opcao-valores")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoOpcaoValorController {

    private final ProdutoOpcaoValorService produtoOpcaoValorService;
    private final ProdutoOpcaoRepository produtoOpcaoRepository;

    public ProdutoOpcaoValorController(
            ProdutoOpcaoValorService produtoOpcaoValorService,
            ProdutoOpcaoRepository produtoOpcaoRepository
    ) {
        this.produtoOpcaoValorService = produtoOpcaoValorService;
        this.produtoOpcaoRepository = produtoOpcaoRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoOpcaoValor>> listar() {
        return ResponseEntity.ok(produtoOpcaoValorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoOpcaoValor> buscarPorId(@PathVariable Long id) {
        return produtoOpcaoValorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ProdutoOpcaoValor produtoOpcaoValor) {

        if (produtoOpcaoValor.getProdutoOpcao() == null
                || produtoOpcaoValor.getProdutoOpcao().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produtoOpcao.id é obrigatório");
        }

        return produtoOpcaoRepository.findById(
                        produtoOpcaoValor.getProdutoOpcao().getId()
                )
                .map(produtoOpcao -> {
                    produtoOpcaoValor.setProdutoOpcao(produtoOpcao);

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(produtoOpcaoValorService.salvar(produtoOpcaoValor));
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoOpcaoValor produtoOpcaoValor
    ) {
        return produtoOpcaoValorService.buscarPorId(id)
                .map(valorExistente -> {

                    if (produtoOpcaoValor.getProdutoOpcao() == null
                            || produtoOpcaoValor.getProdutoOpcao().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produtoOpcao.id é obrigatório");
                    }

                    return produtoOpcaoRepository.findById(
                                    produtoOpcaoValor.getProdutoOpcao().getId()
                            )
                            .map(produtoOpcao -> {

                                valorExistente.setProdutoOpcao(produtoOpcao);
                                valorExistente.setNome(produtoOpcaoValor.getNome());
                                valorExistente.setDescricao(
                                        produtoOpcaoValor.getDescricao()
                                );
                                valorExistente.setValorAdicional(
                                        produtoOpcaoValor.getValorAdicional()
                                );
                                valorExistente.setQuantidadeMinima(
                                        produtoOpcaoValor.getQuantidadeMinima()
                                );
                                valorExistente.setAtivo(
                                        produtoOpcaoValor.getAtivo()
                                );
                                valorExistente.setOrdem(
                                        produtoOpcaoValor.getOrdem()
                                );

                                return ResponseEntity.ok(
                                        produtoOpcaoValorService.salvar(valorExistente)
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

        if (produtoOpcaoValorService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produtoOpcaoValorService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}