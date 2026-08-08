package br.com.atelieritalara.backend.service;

import br.com.atelieritalara.backend.entity.ProdutoImagem;
import br.com.atelieritalara.backend.repository.ProdutoImagemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoImagemService {

    private final ProdutoImagemRepository produtoImagemRepository;

    public ProdutoImagemService(ProdutoImagemRepository produtoImagemRepository) {
        this.produtoImagemRepository = produtoImagemRepository;
    }

    public ProdutoImagem salvar(ProdutoImagem produtoImagem) {
        return produtoImagemRepository.save(produtoImagem);
    }

    public List<ProdutoImagem> listar() {
        return produtoImagemRepository.findAll();
    }

    public Optional<ProdutoImagem> buscarPorId(Long id) {
        return produtoImagemRepository.findById(id);
    }

    public void excluir(Long id) {
        produtoImagemRepository.deleteById(id);
    }
}