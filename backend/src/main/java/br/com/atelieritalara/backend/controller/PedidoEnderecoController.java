package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.PedidoEndereco;
import br.com.atelieritalara.backend.repository.PedidoRepository;
import br.com.atelieritalara.backend.service.PedidoEnderecoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedido-enderecos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoEnderecoController {

    private final PedidoEnderecoService pedidoEnderecoService;
    private final PedidoRepository pedidoRepository;

    public PedidoEnderecoController(
            PedidoEnderecoService pedidoEnderecoService,
            PedidoRepository pedidoRepository
    ) {
        this.pedidoEnderecoService = pedidoEnderecoService;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public ResponseEntity<List<PedidoEndereco>> listar() {
        return ResponseEntity.ok(pedidoEnderecoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoEndereco> buscarPorId(
            @PathVariable Long id
    ) {
        return pedidoEnderecoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody PedidoEndereco pedidoEndereco
    ) {
        if (pedidoEndereco.getPedido() == null
                || pedidoEndereco.getPedido().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("pedido.id é obrigatório");
        }

        var pedido = pedidoRepository.findById(
                pedidoEndereco.getPedido().getId()
        );

        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoEndereco.setPedido(pedido.get());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoEnderecoService.salvar(pedidoEndereco));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody PedidoEndereco pedidoEndereco
    ) {
        return pedidoEnderecoService.buscarPorId(id)
                .map(enderecoExistente -> {

                    if (pedidoEndereco.getPedido() == null
                            || pedidoEndereco.getPedido().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("pedido.id é obrigatório");
                    }

                    var pedido = pedidoRepository.findById(
                            pedidoEndereco.getPedido().getId()
                    );

                    if (pedido.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    enderecoExistente.setPedido(pedido.get());
                    enderecoExistente.setLogradouro(
                            pedidoEndereco.getLogradouro()
                    );
                    enderecoExistente.setNumero(
                            pedidoEndereco.getNumero()
                    );
                    enderecoExistente.setComplemento(
                            pedidoEndereco.getComplemento()
                    );
                    enderecoExistente.setBairro(
                            pedidoEndereco.getBairro()
                    );
                    enderecoExistente.setCidade(
                            pedidoEndereco.getCidade()
                    );
                    enderecoExistente.setEstado(
                            pedidoEndereco.getEstado()
                    );
                    enderecoExistente.setCep(
                            pedidoEndereco.getCep()
                    );

                    return ResponseEntity.ok(
                            pedidoEnderecoService.salvar(enderecoExistente)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (pedidoEnderecoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoEnderecoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}