package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.PedidoEndereco;
import br.com.atelieritalara.backend.repository.PedidoEnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoEnderecoService {

    private final PedidoEnderecoRepository pedidoEnderecoRepository;

    public PedidoEnderecoService(
            PedidoEnderecoRepository pedidoEnderecoRepository
    ) {
        this.pedidoEnderecoRepository = pedidoEnderecoRepository;
    }

    public PedidoEndereco salvar(PedidoEndereco pedidoEndereco) {
        return pedidoEnderecoRepository.save(pedidoEndereco);
    }

    public List<PedidoEndereco> listar() {
        return pedidoEnderecoRepository.findAll();
    }

    public Optional<PedidoEndereco> buscarPorId(Long id) {
        return pedidoEnderecoRepository.findById(id);
    }

    public void excluir(Long id) {
        pedidoEnderecoRepository.deleteById(id);
    }
}