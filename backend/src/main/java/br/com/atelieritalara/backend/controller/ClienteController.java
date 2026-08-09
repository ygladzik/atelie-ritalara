package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.Cliente;
import br.com.atelieritalara.backend.service.ClienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.salvar(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(
            @PathVariable Long id,
            @RequestBody Cliente cliente
    ) {
        return clienteService.buscarPorId(id)
                .map(clienteExistente -> {

                    clienteExistente.setNome(cliente.getNome());
                    clienteExistente.setWhatsapp(cliente.getWhatsapp());
                    clienteExistente.setEmail(cliente.getEmail());
                    clienteExistente.setInstagram(cliente.getInstagram());
                    clienteExistente.setDataNascimento(
                            cliente.getDataNascimento()
                    );
                    clienteExistente.setObservacao(cliente.getObservacao());
                    clienteExistente.setAtivo(cliente.getAtivo());

                    return ResponseEntity.ok(
                            clienteService.salvar(clienteExistente)
                    );
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (clienteService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        clienteService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}