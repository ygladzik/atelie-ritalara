package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.PedidoItem;
import br.com.atelieritalara.backend.repository.PedidoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public PedidoItem salvar(PedidoItem pedidoItem) {
        return pedidoItemRepository.save(pedidoItem);
    }

    public List<PedidoItem> listar() {
        return pedidoItemRepository.findAll();
    }

    public Optional<PedidoItem> buscarPorId(Long id) {
        return pedidoItemRepository.findById(id);
    }

    public void excluir(Long id) {
        pedidoItemRepository.deleteById(id);
    }
}