package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.ProdutoVariacaoAdicional;
import br.com.atelieritalara.backend.repository.ProdutoVariacaoAdicionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoVariacaoAdicionalService {

    private final ProdutoVariacaoAdicionalRepository produtoVariacaoAdicionalRepository;

    public ProdutoVariacaoAdicionalService(
            ProdutoVariacaoAdicionalRepository produtoVariacaoAdicionalRepository
    ) {
        this.produtoVariacaoAdicionalRepository = produtoVariacaoAdicionalRepository;
    }

    public ProdutoVariacaoAdicional salvar(
            ProdutoVariacaoAdicional produtoVariacaoAdicional
    ) {
        return produtoVariacaoAdicionalRepository.save(produtoVariacaoAdicional);
    }

    public List<ProdutoVariacaoAdicional> listar() {
        return produtoVariacaoAdicionalRepository.findAll();
    }

    public Optional<ProdutoVariacaoAdicional> buscarPorId(Long id) {
        return produtoVariacaoAdicionalRepository.findById(id);
    }

    public void excluir(Long id) {
        produtoVariacaoAdicionalRepository.deleteById(id);
    }
}