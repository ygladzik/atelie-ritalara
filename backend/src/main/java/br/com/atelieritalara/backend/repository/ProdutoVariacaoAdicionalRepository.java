package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.ProdutoVariacaoAdicional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoVariacaoAdicionalRepository
        extends JpaRepository<ProdutoVariacaoAdicional, Long> {
}