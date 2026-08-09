package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.PedidoItem;
import br.com.atelieritalara.backend.repository.PedidoRepository;
import br.com.atelieritalara.backend.repository.ProdutoRepository;
import br.com.atelieritalara.backend.repository.ProdutoVariacaoRepository;
import br.com.atelieritalara.backend.service.PedidoItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedido-itens")
@SecurityRequirement(name = "bearerAuth")
public class PedidoItemController {

    private final PedidoItemService pedidoItemService;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoVariacaoRepository produtoVariacaoRepository;

    public PedidoItemController(
            PedidoItemService pedidoItemService,
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository,
            ProdutoVariacaoRepository produtoVariacaoRepository
    ) {
        this.pedidoItemService = pedidoItemService;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.produtoVariacaoRepository = produtoVariacaoRepository;
    }

    @GetMapping
    public ResponseEntity<List<PedidoItem>> listar() {
        return ResponseEntity.ok(pedidoItemService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoItem> buscarPorId(@PathVariable Long id) {
        return pedidoItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoItem pedidoItem) {

        if (pedidoItem.getPedido() == null
                || pedidoItem.getPedido().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("pedido.id é obrigatório");
        }

        if (pedidoItem.getProduto() == null
                || pedidoItem.getProduto().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("produto.id é obrigatório");
        }

        var pedido = pedidoRepository.findById(
                pedidoItem.getPedido().getId()
        );

        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var produto = produtoRepository.findById(
                pedidoItem.getProduto().getId()
        );

        if (produto.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoItem.setPedido(pedido.get());
        pedidoItem.setProduto(produto.get());

        if (pedidoItem.getProdutoVariacao() != null
                && pedidoItem.getProdutoVariacao().getId() != null) {

            var variacao = produtoVariacaoRepository.findById(
                    pedidoItem.getProdutoVariacao().getId()
            );

            if (variacao.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            pedidoItem.setProdutoVariacao(variacao.get());

        } else {
            pedidoItem.setProdutoVariacao(null);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoItemService.salvar(pedidoItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody PedidoItem pedidoItem
    ) {
        return pedidoItemService.buscarPorId(id)
                .map(itemExistente -> {

                    if (pedidoItem.getPedido() == null
                            || pedidoItem.getPedido().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("pedido.id é obrigatório");
                    }

                    if (pedidoItem.getProduto() == null
                            || pedidoItem.getProduto().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("produto.id é obrigatório");
                    }

                    var pedido = pedidoRepository.findById(
                            pedidoItem.getPedido().getId()
                    );

                    if (pedido.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    var produto = produtoRepository.findById(
                            pedidoItem.getProduto().getId()
                    );

                    if (produto.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    itemExistente.setPedido(pedido.get());
                    itemExistente.setProduto(produto.get());

                    if (pedidoItem.getProdutoVariacao() != null
                            && pedidoItem.getProdutoVariacao().getId() != null) {

                        var variacao = produtoVariacaoRepository.findById(
                                pedidoItem.getProdutoVariacao().getId()
                        );

                        if (variacao.isEmpty()) {
                            return ResponseEntity.notFound().build();
                        }

                        itemExistente.setProdutoVariacao(variacao.get());

                    } else {
                        itemExistente.setProdutoVariacao(null);
                    }

                    itemExistente.setQuantidade(
                            pedidoItem.getQuantidade()
                    );
                    itemExistente.setValorUnitario(
                            pedidoItem.getValorUnitario()
                    );
                    itemExistente.setValorTotal(
                            pedidoItem.getValorTotal()
                    );
                    itemExistente.setObservacao(
                            pedidoItem.getObservacao()
                    );

                    return ResponseEntity.ok(
                            pedidoItemService.salvar(itemExistente)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (pedidoItemService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoItemService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}