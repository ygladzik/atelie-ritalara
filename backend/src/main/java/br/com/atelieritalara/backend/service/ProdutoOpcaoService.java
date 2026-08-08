package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.ProdutoOpcao;
import br.com.atelieritalara.backend.repository.ProdutoOpcaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoOpcaoService {

    private final ProdutoOpcaoRepository produtoOpcaoRepository;

    public ProdutoOpcaoService(ProdutoOpcaoRepository produtoOpcaoRepository) {
        this.produtoOpcaoRepository = produtoOpcaoRepository;
    }

    public ProdutoOpcao salvar(ProdutoOpcao produtoOpcao) {
        return produtoOpcaoRepository.save(produtoOpcao);
    }

    public List<ProdutoOpcao> listar() {
        return produtoOpcaoRepository.findAll();
    }

    public Optional<ProdutoOpcao> buscarPorId(Long id) {
        return produtoOpcaoRepository.findById(id);
    }

    public void excluir(Long id) {
        produtoOpcaoRepository.deleteById(id);
    }
}