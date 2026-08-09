package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.PedidoItemImagem;
import br.com.atelieritalara.backend.repository.PedidoItemImagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoItemImagemService {

    private final PedidoItemImagemRepository pedidoItemImagemRepository;

    public PedidoItemImagemService(
            PedidoItemImagemRepository pedidoItemImagemRepository
    ) {
        this.pedidoItemImagemRepository = pedidoItemImagemRepository;
    }

    public PedidoItemImagem salvar(PedidoItemImagem pedidoItemImagem) {
        return pedidoItemImagemRepository.save(pedidoItemImagem);
    }

    public List<PedidoItemImagem> listar() {
        return pedidoItemImagemRepository.findAll();
    }

    public Optional<PedidoItemImagem> buscarPorId(Long id) {
        return pedidoItemImagemRepository.findById(id);
    }

    public void excluir(Long id) {
        pedidoItemImagemRepository.deleteById(id);
    }
}