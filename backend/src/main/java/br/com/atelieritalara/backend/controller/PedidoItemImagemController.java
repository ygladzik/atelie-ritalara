package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.PedidoItemImagem;
import br.com.atelieritalara.backend.repository.PedidoItemRepository;
import br.com.atelieritalara.backend.service.PedidoItemImagemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedido-item-imagens")
@SecurityRequirement(name = "bearerAuth")
public class PedidoItemImagemController {

    private final PedidoItemImagemService pedidoItemImagemService;
    private final PedidoItemRepository pedidoItemRepository;

    public PedidoItemImagemController(
            PedidoItemImagemService pedidoItemImagemService,
            PedidoItemRepository pedidoItemRepository
    ) {
        this.pedidoItemImagemService = pedidoItemImagemService;
        this.pedidoItemRepository = pedidoItemRepository;
    }

    @GetMapping
    public ResponseEntity<List<PedidoItemImagem>> listar() {
        return ResponseEntity.ok(pedidoItemImagemService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoItemImagem> buscarPorId(
            @PathVariable Long id
    ) {
        return pedidoItemImagemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody PedidoItemImagem pedidoItemImagem
    ) {
        if (pedidoItemImagem.getPedidoItem() == null
                || pedidoItemImagem.getPedidoItem().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("pedidoItem.id é obrigatório");
        }

        var pedidoItem = pedidoItemRepository.findById(
                pedidoItemImagem.getPedidoItem().getId()
        );

        if (pedidoItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoItemImagem.setPedidoItem(pedidoItem.get());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoItemImagemService.salvar(pedidoItemImagem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody PedidoItemImagem pedidoItemImagem
    ) {
        return pedidoItemImagemService.buscarPorId(id)
                .map(imagemExistente -> {

                    if (pedidoItemImagem.getPedidoItem() == null
                            || pedidoItemImagem.getPedidoItem().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("pedidoItem.id é obrigatório");
                    }

                    var pedidoItem = pedidoItemRepository.findById(
                            pedidoItemImagem.getPedidoItem().getId()
                    );

                    if (pedidoItem.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    imagemExistente.setPedidoItem(pedidoItem.get());
                    imagemExistente.setUrl(pedidoItemImagem.getUrl());
                    imagemExistente.setOrdem(pedidoItemImagem.getOrdem());

                    return ResponseEntity.ok(
                            pedidoItemImagemService.salvar(imagemExistente)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (pedidoItemImagemService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoItemImagemService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}