package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.ProdutoVariacao;
import br.com.atelieritalara.backend.repository.ProdutoVariacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoVariacaoService {

    private final ProdutoVariacaoRepository produtoVariacaoRepository;

    public ProdutoVariacaoService(
            ProdutoVariacaoRepository produtoVariacaoRepository
    ) {
        this.produtoVariacaoRepository = produtoVariacaoRepository;
    }

    public ProdutoVariacao salvar(ProdutoVariacao produtoVariacao) {
        return produtoVariacaoRepository.save(produtoVariacao);
    }

    public List<ProdutoVariacao> listar() {
        return produtoVariacaoRepository.findAll();
    }

    public Optional<ProdutoVariacao> buscarPorId(Long id) {
        return produtoVariacaoRepository.findById(id);
    }

    public void excluir(Long id) {
        produtoVariacaoRepository.deleteById(id);
    }
}