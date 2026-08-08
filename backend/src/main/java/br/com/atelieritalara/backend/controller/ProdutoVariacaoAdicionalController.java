package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.ProdutoVariacaoAdicional;
import br.com.atelieritalara.backend.repository.ProdutoOpcaoValorRepository;
import br.com.atelieritalara.backend.repository.ProdutoVariacaoRepository;
import br.com.atelieritalara.backend.service.ProdutoVariacaoAdicionalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto-variacao-adicionais")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoVariacaoAdicionalController {

    private final ProdutoVariacaoAdicionalService produtoVariacaoAdicionalService;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;
    private final ProdutoOpcaoValorRepository produtoOpcaoValorRepository;

    public ProdutoVariacaoAdicionalController(
            ProdutoVariacaoAdicionalService produtoVariacaoAdicionalService,
            ProdutoVariacaoRepository produtoVariacaoRepository,
            ProdutoOpcaoValorRepository produtoOpcaoValorRepository
    ) {
        this.produtoVariacaoAdicionalService = produtoVariacaoAdicionalService;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
        this.produtoOpcaoValorRepository = produtoOpcaoValorRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoVariacaoAdicional>> listar() {
        return ResponseEntity.ok(produtoVariacaoAdicionalService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoVariacaoAdicional> buscarPorId(
            @PathVariable Long id
    ) {
        return produtoVariacaoAdicionalService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody ProdutoVariacaoAdicional adicional
    ) {
        if (adicional.getProdutoVariacao() == null
                || adicional.getProdutoVariacao().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produtoVariacao.id é obrigatório");
        }

        if (adicional.getProdutoOpcaoValor() == null
                || adicional.getProdutoOpcaoValor().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produtoOpcaoValor.id é obrigatório");
        }

        var variacao = produtoVariacaoRepository.findById(
                adicional.getProdutoVariacao().getId()
        );

        if (variacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var opcaoValor = produtoOpcaoValorRepository.findById(
                adicional.getProdutoOpcaoValor().getId()
        );

        if (opcaoValor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        adicional.setProdutoVariacao(variacao.get());
        adicional.setProdutoOpcaoValor(opcaoValor.get());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produtoVariacaoAdicionalService.salvar(adicional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoVariacaoAdicional adicional
    ) {
        return produtoVariacaoAdicionalService.buscarPorId(id)
                .map(adicionalExistente -> {

                    if (adicional.getProdutoVariacao() == null
                            || adicional.getProdutoVariacao().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produtoVariacao.id é obrigatório");
                    }

                    if (adicional.getProdutoOpcaoValor() == null
                            || adicional.getProdutoOpcaoValor().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produtoOpcaoValor.id é obrigatório");
                    }

                    var variacao = produtoVariacaoRepository.findById(
                            adicional.getProdutoVariacao().getId()
                    );

                    if (variacao.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    var opcaoValor = produtoOpcaoValorRepository.findById(
                            adicional.getProdutoOpcaoValor().getId()
                    );

                    if (opcaoValor.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    adicionalExistente.setProdutoVariacao(variacao.get());
                    adicionalExistente.setProdutoOpcaoValor(opcaoValor.get());
                    adicionalExistente.setValorAdicional(
                            adicional.getValorAdicional()
                    );

                    return ResponseEntity.ok(
                            produtoVariacaoAdicionalService.salvar(adicionalExistente)
                    );
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (produtoVariacaoAdicionalService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        produtoVariacaoAdicionalService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}