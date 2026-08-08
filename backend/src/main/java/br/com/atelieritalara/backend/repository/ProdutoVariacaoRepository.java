package br.com.atelieritalara.backend.repository;

import br.com.atelieritalara.backend.entity.ProdutoVariacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoVariacaoRepository extends JpaRepository<ProdutoVariacao, Long> {
}