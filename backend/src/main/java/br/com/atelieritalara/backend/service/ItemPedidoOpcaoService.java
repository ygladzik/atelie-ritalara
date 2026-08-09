package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.ItemPedidoOpcao;
import br.com.atelieritalara.backend.repository.ItemPedidoOpcaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemPedidoOpcaoService {

    private final ItemPedidoOpcaoRepository itemPedidoOpcaoRepository;

    public ItemPedidoOpcaoService(
            ItemPedidoOpcaoRepository itemPedidoOpcaoRepository
    ) {
        this.itemPedidoOpcaoRepository = itemPedidoOpcaoRepository;
    }

    public ItemPedidoOpcao salvar(ItemPedidoOpcao itemPedidoOpcao) {
        return itemPedidoOpcaoRepository.save(itemPedidoOpcao);
    }

    public List<ItemPedidoOpcao> listar() {
        return itemPedidoOpcaoRepository.findAll();
    }

    public Optional<ItemPedidoOpcao> buscarPorId(Long id) {
        return itemPedidoOpcaoRepository.findById(id);
    }

    public void excluir(Long id) {
        itemPedidoOpcaoRepository.deleteById(id);
    }
}