package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.Pedido;
import br.com.atelieritalara.backend.repository.ClienteRepository;
import br.com.atelieritalara.backend.repository.UsuarioRepository;
import br.com.atelieritalara.backend.service.PedidoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoController(
            PedidoService pedidoService,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.pedidoService = pedidoService;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {

        if (pedido.getCliente() == null
                || pedido.getCliente().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("cliente.id é obrigatório");
        }

        var cliente = clienteRepository.findById(
                pedido.getCliente().getId()
        );

        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedido.setCliente(cliente.get());

        if (pedido.getUsuario() != null
                && pedido.getUsuario().getId() != null) {

            var usuario = usuarioRepository.findById(
                    pedido.getUsuario().getId()
            );

            if (usuario.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            pedido.setUsuario(usuario.get());
        } else {
            pedido.setUsuario(null);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.salvar(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Pedido pedido
    ) {
        return pedidoService.buscarPorId(id)
                .map(pedidoExistente -> {

                    if (pedido.getCliente() == null
                            || pedido.getCliente().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("cliente.id é obrigatório");
                    }

                    var cliente = clienteRepository.findById(
                            pedido.getCliente().getId()
                    );

                    if (cliente.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    pedidoExistente.setCliente(cliente.get());
                    pedidoExistente.setDataEntrega(
                            pedido.getDataEntrega()
                    );
                    pedidoExistente.setHorarioEntrega(
                            pedido.getHorarioEntrega()
                    );
                    pedidoExistente.setTipoEntrega(
                            pedido.getTipoEntrega()
                    );
                    pedidoExistente.setStatus(
                            pedido.getStatus()
                    );
                    pedidoExistente.setObservacao(
                            pedido.getObservacao()
                    );
                    pedidoExistente.setValorProdutos(
                            pedido.getValorProdutos()
                    );
                    pedidoExistente.setValorEntrega(
                            pedido.getValorEntrega()
                    );
                    pedidoExistente.setValorDesconto(
                            pedido.getValorDesconto()
                    );
                    pedidoExistente.setValorTotal(
                            pedido.getValorTotal()
                    );

                    if (pedido.getUsuario() != null
                            && pedido.getUsuario().getId() != null) {

                        var usuario = usuarioRepository.findById(
                                pedido.getUsuario().getId()
                        );

                        if (usuario.isEmpty()) {
                            return ResponseEntity.notFound().build();
                        }

                        pedidoExistente.setUsuario(usuario.get());

                    } else {
                        pedidoExistente.setUsuario(null);
                    }

                    return ResponseEntity.ok(
                            pedidoService.salvar(pedidoExistente)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (pedidoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        pedidoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}