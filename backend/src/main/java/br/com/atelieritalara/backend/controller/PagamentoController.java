package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.Pagamento;
import br.com.atelieritalara.backend.repository.PedidoRepository;
import br.com.atelieritalara.backend.service.PagamentoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@SecurityRequirement(name = "bearerAuth")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final PedidoRepository pedidoRepository;

    public PagamentoController(
            PagamentoService pagamentoService,
            PedidoRepository pedidoRepository
    ) {
        this.pagamentoService = pagamentoService;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> listar() {
        return ResponseEntity.ok(pagamentoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pagamento pagamento) {

        if (pagamento.getPedido() == null
                || pagamento.getPedido().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("pedido.id é obrigatório");
        }

        var pedido = pedidoRepository.findById(
                pagamento.getPedido().getId()
        );

        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pagamento.setPedido(pedido.get());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pagamentoService.salvar(pagamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Pagamento pagamento
    ) {
        return pagamentoService.buscarPorId(id)
                .map(pagamentoExistente -> {

                    if (pagamento.getPedido() == null
                            || pagamento.getPedido().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("pedido.id é obrigatório");
                    }

                    var pedido = pedidoRepository.findById(
                            pagamento.getPedido().getId()
                    );

                    if (pedido.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    pagamentoExistente.setPedido(pedido.get());
                    pagamentoExistente.setTipo(pagamento.getTipo());
                    pagamentoExistente.setStatus(pagamento.getStatus());
                    pagamentoExistente.setValor(pagamento.getValor());
                    pagamentoExistente.setPercentual(
                            pagamento.getPercentual()
                    );
                    pagamentoExistente.setParcelas(
                            pagamento.getParcelas()
                    );
                    pagamentoExistente.setDataPagamento(
                            pagamento.getDataPagamento()
                    );
                    pagamentoExistente.setLinkPagamento(
                            pagamento.getLinkPagamento()
                    );
                    pagamentoExistente.setObservacao(
                            pagamento.getObservacao()
                    );

                    return ResponseEntity.ok(
                            pagamentoService.salvar(pagamentoExistente)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (pagamentoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pagamentoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}