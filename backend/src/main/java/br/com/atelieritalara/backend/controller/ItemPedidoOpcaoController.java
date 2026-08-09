package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.ItemPedidoOpcao;
import br.com.atelieritalara.backend.repository.PedidoItemRepository;
import br.com.atelieritalara.backend.repository.ProdutoOpcaoRepository;
import br.com.atelieritalara.backend.repository.ProdutoOpcaoValorRepository;
import br.com.atelieritalara.backend.service.ItemPedidoOpcaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item-pedido-opcoes")
@SecurityRequirement(name = "bearerAuth")
public class ItemPedidoOpcaoController {

    private final ItemPedidoOpcaoService itemPedidoOpcaoService;
    private final PedidoItemRepository pedidoItemRepository;
    private final ProdutoOpcaoRepository produtoOpcaoRepository;
    private final ProdutoOpcaoValorRepository produtoOpcaoValorRepository;

    public ItemPedidoOpcaoController(
            ItemPedidoOpcaoService itemPedidoOpcaoService,
            PedidoItemRepository pedidoItemRepository,
            ProdutoOpcaoRepository produtoOpcaoRepository,
            ProdutoOpcaoValorRepository produtoOpcaoValorRepository
    ) {
        this.itemPedidoOpcaoService = itemPedidoOpcaoService;
        this.pedidoItemRepository = pedidoItemRepository;
        this.produtoOpcaoRepository = produtoOpcaoRepository;
        this.produtoOpcaoValorRepository = produtoOpcaoValorRepository;
    }

    @GetMapping
    public ResponseEntity<List<ItemPedidoOpcao>> listar() {
        return ResponseEntity.ok(itemPedidoOpcaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPedidoOpcao> buscarPorId(@PathVariable Long id) {
        return itemPedidoOpcaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody ItemPedidoOpcao itemPedidoOpcao
    ) {
        if (itemPedidoOpcao.getPedidoItem() == null
                || itemPedidoOpcao.getPedidoItem().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("pedidoItem.id é obrigatório");
        }

        if (itemPedidoOpcao.getProdutoOpcao() == null
                || itemPedidoOpcao.getProdutoOpcao().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produtoOpcao.id é obrigatório");
        }

        if (itemPedidoOpcao.getProdutoOpcaoValor() == null
                || itemPedidoOpcao.getProdutoOpcaoValor().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produtoOpcaoValor.id é obrigatório");
        }

        var pedidoItem = pedidoItemRepository.findById(
                itemPedidoOpcao.getPedidoItem().getId()
        );

        if (pedidoItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var produtoOpcao = produtoOpcaoRepository.findById(
                itemPedidoOpcao.getProdutoOpcao().getId()
        );

        if (produtoOpcao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var produtoOpcaoValor = produtoOpcaoValorRepository.findById(
                itemPedidoOpcao.getProdutoOpcaoValor().getId()
        );

        if (produtoOpcaoValor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        itemPedidoOpcao.setPedidoItem(pedidoItem.get());
        itemPedidoOpcao.setProdutoOpcao(produtoOpcao.get());
        itemPedidoOpcao.setProdutoOpcaoValor(produtoOpcaoValor.get());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemPedidoOpcaoService.salvar(itemPedidoOpcao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody ItemPedidoOpcao itemPedidoOpcao
    ) {
        return itemPedidoOpcaoService.buscarPorId(id)
                .map(opcaoExistente -> {

                    if (itemPedidoOpcao.getPedidoItem() == null
                            || itemPedidoOpcao.getPedidoItem().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("pedidoItem.id é obrigatório");
                    }

                    if (itemPedidoOpcao.getProdutoOpcao() == null
                            || itemPedidoOpcao.getProdutoOpcao().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produtoOpcao.id é obrigatório");
                    }

                    if (itemPedidoOpcao.getProdutoOpcaoValor() == null
                            || itemPedidoOpcao.getProdutoOpcaoValor().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produtoOpcaoValor.id é obrigatório");
                    }

                    var pedidoItem = pedidoItemRepository.findById(
                            itemPedidoOpcao.getPedidoItem().getId()
                    );

                    if (pedidoItem.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    var produtoOpcao = produtoOpcaoRepository.findById(
                            itemPedidoOpcao.getProdutoOpcao().getId()
                    );

                    if (produtoOpcao.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    var produtoOpcaoValor = produtoOpcaoValorRepository.findById(
                            itemPedidoOpcao.getProdutoOpcaoValor().getId()
                    );

                    if (produtoOpcaoValor.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    opcaoExistente.setPedidoItem(pedidoItem.get());
                    opcaoExistente.setProdutoOpcao(produtoOpcao.get());
                    opcaoExistente.setProdutoOpcaoValor(produtoOpcaoValor.get());
                    opcaoExistente.setQuantidade(
                            itemPedidoOpcao.getQuantidade()
                    );
                    opcaoExistente.setValorAdicional(
                            itemPedidoOpcao.getValorAdicional()
                    );

                    return ResponseEntity.ok(
                            itemPedidoOpcaoService.salvar(opcaoExistente)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (itemPedidoOpcaoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        itemPedidoOpcaoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}