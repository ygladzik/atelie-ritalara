package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.ProdutoOpcaoValor;
import br.com.atelieritalara.backend.repository.ProdutoOpcaoValorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoOpcaoValorService {

    private final ProdutoOpcaoValorRepository produtoOpcaoValorRepository;

    public ProdutoOpcaoValorService(
            ProdutoOpcaoValorRepository produtoOpcaoValorRepository
    ) {
        this.produtoOpcaoValorRepository = produtoOpcaoValorRepository;
    }

    public ProdutoOpcaoValor salvar(ProdutoOpcaoValor produtoOpcaoValor) {
        return produtoOpcaoValorRepository.save(produtoOpcaoValor);
    }

    public List<ProdutoOpcaoValor> listar() {
        return produtoOpcaoValorRepository.findAll();
    }

    public Optional<ProdutoOpcaoValor> buscarPorId(Long id) {
        return produtoOpcaoValorRepository.findById(id);
    }

    public void excluir(Long id) {
        produtoOpcaoValorRepository.deleteById(id);
    }
}