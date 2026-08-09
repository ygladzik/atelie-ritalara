package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.entity.Endereco;
import br.com.atelieritalara.backend.repository.ClienteRepository;
import br.com.atelieritalara.backend.service.EnderecoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
@SecurityRequirement(name = "bearerAuth")
public class EnderecoController {

    private final EnderecoService enderecoService;
    private final ClienteRepository clienteRepository;

    public EnderecoController(
            EnderecoService enderecoService,
            ClienteRepository clienteRepository
    ) {
        this.enderecoService = enderecoService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listar() {
        return ResponseEntity.ok(enderecoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable Long id) {
        return enderecoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Endereco endereco) {

        if (endereco.getCliente() == null
                || endereco.getCliente().getId() == null) {
            return ResponseEntity.badRequest()
                    .body("cliente.id é obrigatório");
        }

        var cliente = clienteRepository.findById(
                endereco.getCliente().getId()
        );

        if (cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        endereco.setCliente(cliente.get());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enderecoService.salvar(endereco));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody Endereco endereco
    ) {
        return enderecoService.buscarPorId(id)
                .map(enderecoExistente -> {

                    if (endereco.getCliente() == null
                            || endereco.getCliente().getId() == null) {
                        return ResponseEntity.badRequest()
                                .body("cliente.id é obrigatório");
                    }

                    var cliente = clienteRepository.findById(
                            endereco.getCliente().getId()
                    );

                    if (cliente.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    enderecoExistente.setCliente(cliente.get());
                    enderecoExistente.setLogradouro(endereco.getLogradouro());
                    enderecoExistente.setNumero(endereco.getNumero());
                    enderecoExistente.setComplemento(endereco.getComplemento());
                    enderecoExistente.setBairro(endereco.getBairro());
                    enderecoExistente.setCidade(endereco.getCidade());
                    enderecoExistente.setEstado(endereco.getEstado());
                    enderecoExistente.setCep(endereco.getCep());

                    return ResponseEntity.ok(
                            enderecoService.salvar(enderecoExistente)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (enderecoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        enderecoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}